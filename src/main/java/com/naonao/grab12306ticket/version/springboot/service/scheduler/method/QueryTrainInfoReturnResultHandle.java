package com.naonao.grab12306ticket.version.springboot.service.scheduler.method;


import com.naonao.grab12306ticket.version.springboot.constants.TaskStatusName;
import com.naonao.grab12306ticket.version.springboot.entity.database.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.StatusInformationEntity;
import com.naonao.grab12306ticket.version.springboot.mapper.StatusInformationMapper;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.booking.BookingReturnResult;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.login.LoginReturnResult;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.springboot.service.base.AbstractScheduler;
import com.naonao.grab12306ticket.version.springboot.service.notification.Notification;
import com.naonao.grab12306ticket.version.springboot.service.ticket.booking.Booking;
import com.naonao.grab12306ticket.version.springboot.service.ticket.login.Login;
import com.naonao.grab12306ticket.version.springboot.service.tools.GeneralTools;
import com.naonao.grab12306ticket.version.springboot.service.tools.HttpTools;
import com.naonao.grab12306ticket.version.springboot.util.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 22:13
 **/
@Slf4j
public class QueryTrainInfoReturnResultHandle extends AbstractScheduler {

    // @Autowired
    private StatusInformationMapper statusInformationMapper;

    private String message;

    /**
     * here is QueryTrainInfoReturnResult handle method
     */

    private BookingReturnResult bookingReturnResult;

    private QueryTrainInfoReturnResult queryTrainInfoReturnResult;


    /**
     * database version
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     */
    public QueryTrainInfoReturnResultHandle(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        this.queryTrainInfoReturnResult = queryTrainInfoReturnResult;

        this.statusInformationMapper = ApplicationContextProvider.getBean(StatusInformationMapper.class);
    }


    /**
     * database version
     *
     * QueryTrainInfoReturnResult handle method
     * first get a QueryTrainInfoReturnResult object
     *
     *  1. check queryTrainInfoReturnResult is true, else update database.
     *  2. check bookingReturnResult is true, else update database.
     *  3. check send notification is true, else update database.
     *  4. booking succeed, update database.
     *
     */
    public void handle()throws Exception{
        // // test
        // int sleepTime = RandomUtils.nextInt(1000, 3000);
        // try{
        //     Thread.sleep(sleepTime);
        // }catch (InterruptedException e){
        //     log.error(e.getMessage());
        // }
        // log.info("i sleep time ---> " + sleepTime + "   hash ---> " + queryTrainInfoReturnResult.getUserInformationEntity().getHash());

        StatusInformationEntity statusInformationEntity = queryTrainInfoReturnResult.getStatusInformationEntity();
        // not found eligible train information
        if (!isTure(queryTrainInfoReturnResult)){
            message = queryTrainInfoReturnResult.getMessage();
            updateDatabase(TaskStatusName.WAIT, statusInformationEntity, message);
            return;
        }
        // booking failed
        if (!booking(queryTrainInfoReturnResult)){
            // this message get in method.
            updateDatabase(TaskStatusName.BOOKING_FAILED, statusInformationEntity, message);
            return;
        }
        // send notification failed
        if (!sendNotification(bookingReturnResult, queryTrainInfoReturnResult.getNotificationInformationEntity())){
            message = TaskStatusName.BOOKING_SUCCEED_BUT_SEND_NOTIFICATION_FAILED.getTaskStatusName();
            updateDatabase(TaskStatusName.BOOKING_SUCCEED_BUT_SEND_NOTIFICATION_FAILED, statusInformationEntity, message);
            return;
        }
        // success
        message = TaskStatusName.COMPLETED.getTaskStatusName();
        updateDatabase(TaskStatusName.COMPLETED, statusInformationEntity, message);
    }


    /**
     * check queryTrainInfoReturnResult is true.
     * if true, then have train information.
     *
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     * @return                              Boolean
     */
    private Boolean isTure(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        return queryTrainInfoReturnResult.getStatus();
    }

    /**
     * booking ticket
     *
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     * @return                              Boolean
     */
    private Boolean booking(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        // produce
        String username12306 = queryTrainInfoReturnResult.getUserInformationEntity().getUsername12306();
        String password12306 = queryTrainInfoReturnResult.getUserInformationEntity().getPassword12306();
        // Login
        CloseableHttpClient session = HttpTools.getSession(30000);
        Login login = new Login(session);
        LoginReturnResult loginReturnResult = login.login(username12306, password12306);
        if (!loginReturnResult.getStatus()){
            log.error(loginReturnResult.getMessage());
            return false;
        }
        session = loginReturnResult.getSession();
        // Booking
        Booking booking = new Booking(session);
        BookingReturnResult bookingReturnResult = booking.booking(queryTrainInfoReturnResult);
        message = bookingReturnResult.getMessage();
        if (!bookingReturnResult.getStatus()){
            log.error(bookingReturnResult.getMessage());
            return false;
        }
        this.bookingReturnResult = bookingReturnResult;
        return true;

        // // test
        // int a = RandomUtils.nextInt(0, 100);
        // if (a % 2 == 0){
        //     return true;
        // }
        // return false;
    }

    /**
     * send notification
     *
     * @param bookingReturnResult               bookingReturnResult
     * @param notificationInformationEntity     notificationInformationEntity
     * @return                                  Boolean
     */
    private Boolean sendNotification(BookingReturnResult bookingReturnResult, NotificationInformationEntity notificationInformationEntity)throws Exception{

        // produce
        // here is send notification method
        if (bookingReturnResult == null){
            log.error(BOOKINGRETURNRESULT_IS_NULL);
            return false;
        }
        return new Notification().sendNotification(bookingReturnResult, notificationInformationEntity);

        // test
        // BookingReturnResult bookingReturnResult1 = new BookingReturnResult();
        // bookingReturnResult1.setStatus(true);
        // bookingReturnResult1.setSession(null);
        // bookingReturnResult1.setMessage(SUCCESS);
        // bookingReturnResult1.setBookingResultString(SUCCESS);
        // int a = RandomUtils.nextInt(0, 100);
        // if (a % 2 == 0){
        //     return true;
        // }
        // return false;
    }

    /**
     * update database
     * @param statusName                statusName
     * @param statusInformationEntity    statusInformationEntity
     */
    private void updateDatabase(TaskStatusName statusName, StatusInformationEntity statusInformationEntity, String message){
        String status = statusName.getTaskStatusName();
        String lastRunningTime = GeneralTools.currentDateAndTime();
        String endTime = GeneralTools.currentDateAndTime();
        Long tryCount = statusInformationEntity.getTryCount() + 1L;
        statusInformationEntity.setStatus(status);
        statusInformationEntity.setTaskLastRunTime(lastRunningTime);
        statusInformationEntity.setTaskEndTime(endTime);
        statusInformationEntity.setTryCount(tryCount);
        statusInformationEntity.setMessage(message);
        statusInformationMapper.update(statusInformationEntity);
    }





}
