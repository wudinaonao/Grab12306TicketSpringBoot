package com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.runnable.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.naonao.grab12306ticket.version.springboot.constants.TaskStatusName;
import com.naonao.grab12306ticket.version.springboot.entity.database.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.StatusInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.UserInformationEntity;
import com.naonao.grab12306ticket.version.springboot.mapper.GrabTicketInformationMapper;
import com.naonao.grab12306ticket.version.springboot.mapper.NotificationInformationMapper;
import com.naonao.grab12306ticket.version.springboot.mapper.StatusInformationMapper;
import com.naonao.grab12306ticket.version.springboot.mapper.UserInformationMapper;
import com.naonao.grab12306ticket.version.springboot.service.base.AbstractScheduler;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.runnable.QueryTrainInfoReturnResultProducer;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.strategy.RejectExecutionHandlerBlocking;
import com.naonao.grab12306ticket.version.springboot.service.ticket.query.arguments.QueryTrainInfoArguments;
import com.naonao.grab12306ticket.version.springboot.service.tools.GeneralTools;
import com.naonao.grab12306ticket.version.springboot.util.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 21:56
 **/
@Slf4j
public class ProducerPool extends AbstractScheduler implements Runnable {

    /**
     * Queue for storage queryTrainInfoReturnResult
     */
    private QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue;

    /**
     * database mapper
     */
    private UserInformationMapper userInformationMapper;
    private GrabTicketInformationMapper grabTicketInformationMapper;
    private NotificationInformationMapper notificationInformationMapper;
    private StatusInformationMapper statusInformationMapper;

    /**
     * database version
     * @param queryTrainInfoReturnResultQueue   queryTrainInfoReturnResultQueue
     */
    public ProducerPool(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue){
        this.userInformationMapper = ApplicationContextProvider.getBean(UserInformationMapper.class);
        this.grabTicketInformationMapper = ApplicationContextProvider.getBean(GrabTicketInformationMapper.class);
        this.notificationInformationMapper = ApplicationContextProvider.getBean(NotificationInformationMapper.class);
        this.statusInformationMapper = ApplicationContextProvider.getBean(StatusInformationMapper.class);
        this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
    }


    @Override
    public void run() {
        while (true){
            // if 12306 system is maintenance, then continue
            if (GeneralTools.systemMaintenanceTime()){
                continue;
            }
            produce();
        }
    }

    /**
     * get information from database status table, then encapsulation QueryTrainInfoArguments
     * object for get QueryTrainInfoReturnResult object, then put QueryTrainInfoReturnResult to
     * queue
     */
    private void produce(){
        // produce thread pool
        ExecutorService pool = createThreadPool();
        while (true){
            // get queryTrainInfoArguments List
            List<QueryTrainInfoArguments> queryTrainInfoArgumentsList = getQueryTrainInfoArgumentsList();
            if (queryTrainInfoArgumentsList == null){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    continue;
                }
                continue;
            }
            for (QueryTrainInfoArguments queryTrainInfoArguments: queryTrainInfoArgumentsList){
                if (queryTrainInfoArguments != null){
                    // if current date > tran date
                    if (!checkTrainDate(queryTrainInfoArguments)){
                        deleteColumnByDatabase(queryTrainInfoArguments);
                        continue;
                    }
                    log.info("get task from database. hash ---> " + queryTrainInfoArguments.getHash());
                    pool.execute(new QueryTrainInfoReturnResultProducer(
                            queryTrainInfoReturnResultQueue,
                            queryTrainInfoArguments
                    ));
                    // update task status is RUNNING
                    // updateDatabase(queryTrainInfoArguments);
                }
            }
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                continue;
            }
        }
    }

    private ExecutorService createThreadPool(){
        int corePoolSize = 32;
        int maximumPoolSize = 64;
        long keepAliveTime = 60L;
        int queueSize = 128;

        // create thread pool
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Produce QueryTrainInfoReturnResult[%d]")
                .build();
        return  new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize),
                threadFactory,
                new RejectExecutionHandlerBlocking()
        );
    }

    /**
     * get QueryTrainInfoArguments object list from database
     * if status not completed, then get
     * @return  List<QueryTrainInfoArguments>
     */
    private List<QueryTrainInfoArguments> getQueryTrainInfoArgumentsList(){
        List<StatusInformationEntity> statusInformationEntityList = statusInformationMapper.getStatusInformationTableListByUnfinished();
        if (statusInformationEntityList.size() <= 0){
            return null;
        }
        List<QueryTrainInfoArguments> queryTrainInfoArgumentsList = new ArrayList<>();
        for (StatusInformationEntity statusInformationEntity: statusInformationEntityList){
            QueryTrainInfoArguments queryTrainInfoArguments = makeQueryTrainInfoArguments(statusInformationEntity);
            if (queryTrainInfoArguments != null){
                queryTrainInfoArgumentsList.add(queryTrainInfoArguments);
            }
        }
        if (queryTrainInfoArgumentsList.size() <= 0){
            return null;
        }
        return queryTrainInfoArgumentsList;
    }


    /**
     * make QueryTrainInfoArguments object by statusEntity object
     *
     * first get hash from statusEntity, then get UserInformationEntity,
     * GrabTicketInformationEntity, NotificationInformationEntity object.
     * second get each property value used to encapsulate the
     * QueryTrainInfoArguments object
     *
     * @param statusInformationEntity    statusInformationEntity
     * @return                          QueryTrainInfoArguments
     */
    private QueryTrainInfoArguments makeQueryTrainInfoArguments(StatusInformationEntity statusInformationEntity){
        // get data
        String hash = statusInformationEntity.getHash();
        UserInformationEntity userInformationEntity = userInformationMapper.getUsernameAndPasswordByHash(hash);
        GrabTicketInformationEntity grabTicketInformationEntity = grabTicketInformationMapper.getGrabTicketInformationByHash(hash);
        NotificationInformationEntity notificationInformationEntity = notificationInformationMapper.getNotificationInformationByHash(hash);
        // if 4 table not simultaneously exist, return a null
        if (userInformationEntity == null || grabTicketInformationEntity == null || notificationInformationEntity == null){
            return null;
        }
        String beforeTime = grabTicketInformationEntity.getBeforeTime();
        String afterTime = grabTicketInformationEntity.getAfterTime();
        String trainDate = grabTicketInformationEntity.getTrainDate();
        String fromStation = grabTicketInformationEntity.getFromStation();
        String toStation = grabTicketInformationEntity.getToStation();
        String purposeCode = grabTicketInformationEntity.getPurposeCode();
        String trainName = grabTicketInformationEntity.getTrainName();
        // instance a QueryTrainInfoArguments
        QueryTrainInfoArguments queryTrainInfoArguments = new QueryTrainInfoArguments();
        queryTrainInfoArguments.setBeforeTime(beforeTime);
        queryTrainInfoArguments.setAfterTime(afterTime);
        queryTrainInfoArguments.setTrainDate(trainDate);
        queryTrainInfoArguments.setFromStation(fromStation);
        queryTrainInfoArguments.setToStation(toStation);
        queryTrainInfoArguments.setPurposeCode(purposeCode);
        queryTrainInfoArguments.setTrainName(trainName);
        queryTrainInfoArguments.setHash(hash);
        queryTrainInfoArguments.setUserInformationEntity(userInformationEntity);
        queryTrainInfoArguments.setGrabTicketInformationEntity(grabTicketInformationEntity);
        queryTrainInfoArguments.setNotificationInformationEntity(notificationInformationEntity);
        queryTrainInfoArguments.setStatusInformationEntity(statusInformationEntity);
        if (!checkQueryTrainInfoArguments(queryTrainInfoArguments)){
            return null;
        }
        return queryTrainInfoArguments;
    }

    /**
     * check if input queryTrainInfoArguments instance attributes has null value
     * @param queryTrainInfoArguments   queryTrainInfoArguments
     * @return                          boolean
     */
    private Boolean checkQueryTrainInfoArguments(QueryTrainInfoArguments queryTrainInfoArguments){
        Class cls = queryTrainInfoArguments.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field: fields){
            field.setAccessible(true);
            Object value = null;
            try{
                value = field.get(queryTrainInfoArguments);
            }catch (IllegalAccessException e){
                return false;
            }
            if (value == null){
                return false;
            }
        }
        return true;
    }

    /**
     * get a queryTrainInfoArguments object,
     * then update database status table status column is RUNNING.
     * @param queryTrainInfoArguments   queryTrainInfoArguments
     */
    private void updateDatabase(QueryTrainInfoArguments queryTrainInfoArguments){
        StatusInformationEntity statusInformationEntity = queryTrainInfoArguments.getStatusInformationEntity();
        statusInformationEntity.setStatus(TaskStatusName.RUNNING.getTaskStatusName());
        statusInformationMapper.update(statusInformationEntity);
    }

    /**
     * delete task from database all table
     * @param queryTrainInfoArguments   queryTrainInfoArguments
     */
    private void deleteColumnByDatabase(QueryTrainInfoArguments queryTrainInfoArguments){
        UserInformationEntity userInformationEntity = queryTrainInfoArguments.getUserInformationEntity();
        GrabTicketInformationEntity grabTicketInformationEntity = queryTrainInfoArguments.getGrabTicketInformationEntity();
        NotificationInformationEntity notificationInformationEntity = queryTrainInfoArguments.getNotificationInformationEntity();
        StatusInformationEntity statusInformationEntity = queryTrainInfoArguments.getStatusInformationEntity();
        userInformationMapper.delete(userInformationEntity);
        grabTicketInformationMapper.delete(grabTicketInformationEntity);
        notificationInformationMapper.delete(notificationInformationEntity);
        statusInformationMapper.delete(statusInformationEntity);
    }

    /**
     * check if the train date is valid.
     * @param queryTrainInfoArguments   queryTrainInfoArguments
     * @return                          Boolean
     */
    private Boolean checkTrainDate(QueryTrainInfoArguments queryTrainInfoArguments){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        Date trainDate;
        try{
            trainDate = simpleDateFormat.parse(queryTrainInfoArguments.getTrainDate());
        }catch (ParseException e){
            log.error(e.getMessage());
            return false;
        }
        String currentDateString = simpleDateFormat.format(new Date()).replace("-","").trim();
        String trainDateString = queryTrainInfoArguments.getTrainDate().replace("-","").trim();
        boolean isSameDay = currentDateString.equals(trainDateString);
        return currentDate.before(trainDate) || isSameDay;
    }

}
