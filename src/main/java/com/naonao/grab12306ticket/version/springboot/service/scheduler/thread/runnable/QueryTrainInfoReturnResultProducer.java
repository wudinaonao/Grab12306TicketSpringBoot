package com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.runnable;


import com.naonao.grab12306ticket.version.springboot.constants.TaskStatusName;
import com.naonao.grab12306ticket.version.springboot.entity.database.StatusInformationEntity;
import com.naonao.grab12306ticket.version.springboot.mapper.StatusInformationMapper;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.springboot.service.base.AbstractScheduler;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.springboot.service.ticket.query.QueryTrainInfo;
import com.naonao.grab12306ticket.version.springboot.service.ticket.query.arguments.QueryTrainInfoArguments;
import com.naonao.grab12306ticket.version.springboot.service.tools.GeneralTools;
import com.naonao.grab12306ticket.version.springboot.service.tools.HttpTools;
import com.naonao.grab12306ticket.version.springboot.temp.test.CreateQueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.springboot.util.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-07 22:06
 **/
@Slf4j
public class QueryTrainInfoReturnResultProducer extends AbstractScheduler implements Runnable{

    // @Autowired
    private StatusInformationMapper statusInformationMapper;

    private QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue;
    private QueryTrainInfoArguments queryTrainInfoArguments;


    public QueryTrainInfoReturnResultProducer(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue,
                                              QueryTrainInfoArguments queryTrainInfoArguments){
        this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
        this.queryTrainInfoArguments = queryTrainInfoArguments;
        this.statusInformationMapper = ApplicationContextProvider.getBean(StatusInformationMapper.class);
    }

    /**
     * database version
     * according to arguments execute queryTrainInfo method, then put result to queue.
     */
    @Override
    public void run() {
        // produce
        log.info("query ticket information from 12306, task hash ---> " + queryTrainInfoArguments.getHash());
        QueryTrainInfo queryTrainInfo = new QueryTrainInfo(HttpTools.getSession(30000));
        QueryTrainInfoReturnResult queryTrainInfoReturnResult = queryTrainInfo.queryTrainInfo(queryTrainInfoArguments);
        updateDatabase(TaskStatusName.RUNNING, queryTrainInfoReturnResult.getStatusInformationEntity());
        queryTrainInfoReturnResultQueue.producer(queryTrainInfoReturnResult);
        log.info("put a information to queue, hash ---> " + queryTrainInfoArguments.getHash());
        // test
        // test();

    }

    private void test(){
        CreateQueryTrainInfoReturnResult createQueryTrainInfoReturnResult = new CreateQueryTrainInfoReturnResult(queryTrainInfoArguments);
        QueryTrainInfoReturnResult queryTrainInfoReturnResult = createQueryTrainInfoReturnResult.queryTrainInfoReturnResult();
        log.info("put a queryTrainInfoReturnResult to queue, hash ---> " + queryTrainInfoReturnResult.getUserInformationEntity().getHash());
        updateDatabase(TaskStatusName.RUNNING, queryTrainInfoReturnResult.getStatusInformationEntity());
        queryTrainInfoReturnResultQueue.producer(createQueryTrainInfoReturnResult.queryTrainInfoReturnResult());
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

    private void updateDatabase(TaskStatusName statusName, StatusInformationEntity statusInformationEntity){
        String status = statusName.getTaskStatusName();
        String lastRunningTime = GeneralTools.currentDateAndTime();
        String endTime = GeneralTools.currentDateAndTime();
        Long tryCount = statusInformationEntity.getTryCount() + 1L;
        String message = statusName.getTaskStatusName();
        statusInformationEntity.setStatus(status);
        statusInformationEntity.setTaskLastRunTime(lastRunningTime);
        statusInformationEntity.setTaskEndTime(endTime);
        statusInformationEntity.setTryCount(tryCount);
        statusInformationEntity.setMessage(message);
        statusInformationMapper.update(statusInformationEntity);
    }

}
