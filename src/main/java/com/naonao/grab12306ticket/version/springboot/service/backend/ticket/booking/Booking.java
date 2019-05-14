package com.naonao.grab12306ticket.version.springboot.service.backend.ticket.booking;


import com.naonao.grab12306ticket.version.database.backend.resultclass.ticket.booking.*;
import com.naonao.grab12306ticket.version.database.backend.resultclass.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.database.backend.ticket.booking.common.AbstractBooking;
import com.naonao.grab12306ticket.version.database.backend.ticket.login.CheckUserStatus;

import com.naonao.grab12306ticket.version.database.backend.tools.GeneralTools;
import lombok.extern.log4j.Log4j;
import org.apache.http.impl.client.CloseableHttpClient;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-01 23:53
 **/
@Log4j
public class Booking extends AbstractBooking {

    

    private String secretStr;
    private String trainDate;
    private String backTrainDate;
    private String purposeCode;
    private String queryFromStationName;
    private String queryToStationName;
    private String passengerName;
    private String documentType;
    private String documentNumber;
    private String mobile;
    private String[] seatTypeArr;
    private String expectSeatNumber;


    public Booking(CloseableHttpClient session){
        this.session = session;
    }

    public BookingReturnResult booking(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        // set variable
        setVariable(queryTrainInfoReturnResult);

        // 1 --> check user login status
        log.info(CHECK_LOGIN_STATUS);
        boolean userStatus = checkUserStatus();
        if (!userStatus){
            log.error(USER_NOT_LOGGED_IN);
            return failedBookingReturnResult(session, USER_NOT_LOGGED_IN);
        }

        // 2 --> submit order request
        log.info(SUBMIT_ORDER_REQUEST);
        SubmitOrderRequestReturnResult submitOrderRequestReturnResult = submitOrderRequest(
                secretStr,
                trainDate,
                backTrainDate,
                purposeCode,
                queryFromStationName,
                queryToStationName
        );
        if (!submitOrderRequestReturnResult.getStatus()){
            log.error(submitOrderRequestReturnResult.getMessage());
            return failedBookingReturnResult(session, submitOrderRequestReturnResult.getMessage());
        }
        session = submitOrderRequestReturnResult.getSession();

        // 3 --> initialization data
        log.info(GET_INITIALIZATION_DATA_AND_SET);
        InitDcReturnResult initDcReturnResult = initDc();
        if (!initDcReturnResult.getStatus()){
            log.error(initDcReturnResult.getMessage());
            return failedBookingReturnResult(session, initDcReturnResult.getMessage());
        }
        session = initDcReturnResult.getSession();

        // 4 --> get passenger DTO
        log.info(GET_PASSENGER_DTO);
        GetPassengerDTOsReturnResult getPassengerDTOsReturnResult = getPassengerDTOs(initDcReturnResult.getRepeatSubmitToken());
        if (!getPassengerDTOsReturnResult.getStatus()){
            log.error(getPassengerDTOsReturnResult.getMessage());
            return failedBookingReturnResult(session, getPassengerDTOsReturnResult.getMessage());
        }
        session = getPassengerDTOsReturnResult.getSession();

        // 5 --> check order information
        log.info(CHECK_ORDER_INFORMATION);
        CheckOrderInfoReturnResult checkOrderInfoReturnResult = checkOrderInfo(
                seatTypeArr,
                passengerName,
                documentType,
                documentNumber,
                mobile,
                initDcReturnResult.getRepeatSubmitToken(),
                initDcReturnResult.getLeftDetails()
        );
        if (!checkOrderInfoReturnResult.getStatus()){
            log.error(checkOrderInfoReturnResult.getMessage());
            return failedBookingReturnResult(session, checkOrderInfoReturnResult.getMessage());
        }
        session = checkOrderInfoReturnResult.getSession();

        // 6 --> get queue count information
        log.info(GET_QUEUE_COUNT_INFORMATION);
        GetQueueCountReturnResult getQueueCountReturnResult = getQueueCount(
                checkOrderInfoReturnResult.getSeatType(),
                initDcReturnResult.getRepeatSubmitToken(),
                initDcReturnResult.getLeftTicketStr(),
                initDcReturnResult.getTrainDateTime(),
                initDcReturnResult.getFromStationTelecode(),
                initDcReturnResult.getPurposeCodes(),
                initDcReturnResult.getStationTrainCode(),
                initDcReturnResult.getToStationTelecode(),
                initDcReturnResult.getTrainLocation(),
                initDcReturnResult.getTrainNo()
        );
        if (!getQueueCountReturnResult.getStatus()){
            log.error(getQueueCountReturnResult.getMessage());
            return failedBookingReturnResult(session, getQueueCountReturnResult.getMessage());
        }
        session = getQueueCountReturnResult.getSession();

        // 7 --> get confirm information
        log.info(GET_CONFIRM_INFORMATION);
        ConfirmSingleForQueueReturnResult confirmSingleForQueueReturnResult = confirmSingleForQueue(
                expectSeatNumber,
                checkOrderInfoReturnResult.getPassengerTicketStr(),
                checkOrderInfoReturnResult.getOldPassengerStr(),
                initDcReturnResult.getKeyCheckIsChange(),
                initDcReturnResult.getLeftTicketStr(),
                initDcReturnResult.getPurposeCodes(),
                initDcReturnResult.getRepeatSubmitToken(),
                initDcReturnResult.getTrainLocation()
        );
        if (!confirmSingleForQueueReturnResult.getStatus()){
            log.error(confirmSingleForQueueReturnResult.getMessage());
            return failedBookingReturnResult(session, confirmSingleForQueueReturnResult.getMessage());
        }
        session = confirmSingleForQueueReturnResult.getSession();

        // 8 --> query order wait time
        log.info(QUERY_ORDER_WAIT_TIME);
        QueryOrderWaitTimeReturnResult queryOrderWaitTimeReturnResult = queryOrderWaitTime(initDcReturnResult.getRepeatSubmitToken());
        if (!queryOrderWaitTimeReturnResult.getStatus()){
            log.info(QUERY_ORDER_WAIT_TIME_FAILED);
            return successBookingReturnResult(session, BOOKING_MAY_HAVE_BEEN_SUCCESSFUL);

        }
        session = queryOrderWaitTimeReturnResult.getSession();

        // 9 --> try get order result from queue
        log.info(TRY_GET_ORDER_RESULT_FROM_QUEUE);
        ResultOrderForDcQueueReturnResult resultOrderForDcQueueReturnResult = resultOrderForDcQueue(
                queryOrderWaitTimeReturnResult.getOrderId(),
                initDcReturnResult.getRepeatSubmitToken()
        );
        if (!resultOrderForDcQueueReturnResult.getStatus()){
            log.error(resultOrderForDcQueueReturnResult.getMessage());
            return failedBookingReturnResult(session, resultOrderForDcQueueReturnResult.getMessage());
        }
        session = resultOrderForDcQueueReturnResult.getSession();

        // 10 --> get booking result
        log.info(GET_BOOKING_RESULT);
        ResultBookingTicketHtmlReturnResult resultBookingTicketHtmlReturnResult = resultBookingTicketHtml(initDcReturnResult.getRepeatSubmitToken());
        if (!resultBookingTicketHtmlReturnResult.getStatus()){
            log.error(resultBookingTicketHtmlReturnResult.getMessage());
            return failedBookingReturnResult(session, resultBookingTicketHtmlReturnResult.getMessage());
        }
        session = resultBookingTicketHtmlReturnResult.getSession();

        // success
        return successBookingReturnResult(
                session,
                BOOKING_SUCCESS,
                // result string
                bookingTicketResultToString(resultBookingTicketHtmlReturnResult),
                // result object
                resultBookingTicketHtmlReturnResult
        );

    }

    private void setVariable(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        secretStr = queryTrainInfoReturnResult.getTrainInfoList().get(0).getSecretStr();
        trainDate = formatDate(queryTrainInfoReturnResult.getTrainInfoList().get(0).getTrainDate());
        // backTrainDate is current date
        backTrainDate = GeneralTools.currentDate();
        purposeCode = queryTrainInfoReturnResult.getGrabTicketInformationTable().getPurposeCode();
        queryFromStationName = queryTrainInfoReturnResult.getGrabTicketInformationTable().getFromStation();
        queryToStationName = queryTrainInfoReturnResult.getGrabTicketInformationTable().getToStation();
        passengerName = queryTrainInfoReturnResult.getGrabTicketInformationTable().getPassengerName();
        documentType = queryTrainInfoReturnResult.getGrabTicketInformationTable().getDocumentType();
        documentNumber = queryTrainInfoReturnResult.getGrabTicketInformationTable().getDocumentNumber();
        mobile = queryTrainInfoReturnResult.getGrabTicketInformationTable().getMobile();
        seatTypeArr = queryTrainInfoReturnResult.getGrabTicketInformationTable().getSeatType().split(",");
        expectSeatNumber = queryTrainInfoReturnResult.getGrabTicketInformationTable().getExpectSeatNumber();
    }

    private Boolean checkUserStatus(){
        CheckUserStatus checkUserStatus = new CheckUserStatus(session);
        return checkUserStatus.checkUserStatus();
    }

    private SubmitOrderRequestReturnResult submitOrderRequest(String secretStr,
                                                              String trainDate,
                                                              String backTrainDate,
                                                              String purposeCode,
                                                              String queryFromStationName,
                                                              String queryToStationName){
        SubmitOrderRequest submitOrderRequest = new SubmitOrderRequest(session);
        return submitOrderRequest.submitOrderRequest(
                secretStr,
                trainDate,
                backTrainDate,
                purposeCode,
                queryFromStationName,
                queryToStationName
        );
    }

    private InitDcReturnResult initDc(){
        InitDc initDc = new InitDc(session);
        return initDc.initDc();
    }

    private GetPassengerDTOsReturnResult getPassengerDTOs(String repeatSubmitToken){
        GetPassengerDTOs getPassengerDTOs = new GetPassengerDTOs(session);
        return getPassengerDTOs.getPassengerDto(repeatSubmitToken);
    }

    private CheckOrderInfoReturnResult checkOrderInfo(String[] seatTypeArr,
                                                      String passengerName,
                                                      String documentType,
                                                      String documentNumber,
                                                      String mobile,
                                                      String repeatSubmitToken,
                                                      String[] leftDetails){
        CheckOrderInfo checkOrderInfo = new CheckOrderInfo(session);
        return checkOrderInfo.checkOrderInfo(
                seatTypeArr,
                passengerName,
                documentType,
                documentNumber,
                mobile,
                repeatSubmitToken,
                leftDetails
        );
    }

    private GetQueueCountReturnResult getQueueCount(String seatType,
                                                    String repeatSubmitToken,
                                                    String leftTicketStr,
                                                    String trainDateTime,
                                                    String fromStationTelecode,
                                                    String purposeCodes,
                                                    String stationTrainCode,
                                                    String toStationTelecode,
                                                    String trainLocation,
                                                    String trainNo){
        GetQueueCount getQueueCount = new GetQueueCount(session);
        return getQueueCount.getQueueCount(
                seatType,
                repeatSubmitToken,
                leftTicketStr,
                trainDateTime,
                fromStationTelecode,
                purposeCodes,
                stationTrainCode,
                toStationTelecode,
                trainLocation,trainNo
        );
    }

    private ConfirmSingleForQueueReturnResult confirmSingleForQueue(String expectSeatNumber,
                                                                    String passengerTicketStr,
                                                                    String oldPassengerStr,
                                                                    String keyCheckIsChange,
                                                                    String leftTicketStr,
                                                                    String purposeCodes,
                                                                    String repeatSubmitToken,
                                                                    String trainLocation){
        ConfirmSingleForQueue confirmSingleForQueue = new ConfirmSingleForQueue(session);
        return confirmSingleForQueue.confirmSingleForQueue(
                expectSeatNumber,
                passengerTicketStr,
                oldPassengerStr,
                keyCheckIsChange,
                leftTicketStr,
                purposeCodes,
                repeatSubmitToken,
                trainLocation
        );
    }

    private QueryOrderWaitTimeReturnResult queryOrderWaitTime(String repeatSubmitToken){
        QueryOrderWaitTime queryOrderWaitTime = new QueryOrderWaitTime(session);
        return queryOrderWaitTime.queryOrderWaitTime(repeatSubmitToken);
    }

    private ResultOrderForDcQueueReturnResult resultOrderForDcQueue(String orderId,
                                                                    String repeatSubmitToken){
        ResultOrderForDcQueue resultOrderForDcQueue = new ResultOrderForDcQueue(session);
        return resultOrderForDcQueue.resultOrderForQueue(orderId, repeatSubmitToken);
    }

    private ResultBookingTicketHtmlReturnResult resultBookingTicketHtml(String repeatSubmitToken){
        ResultBookingTicketHtml resultBookingTicketHtml = new ResultBookingTicketHtml(session);
        return resultBookingTicketHtml.resultBookingTicketHtml(repeatSubmitToken);
    }

    /**
     * get booking result string
     *
     * @return      booking result
     */
    private String bookingTicketResultToString(ResultBookingTicketHtmlReturnResult resultBookingTicketHtmlReturnResult){
        // BookingTicketResultInfo bookingTicketResultInfo = new BookingTicketResultInfo(bookingTicketResultStr);
        StringBuilder resultStr = new StringBuilder();
        String[] content = {
                "The seat is locked, please pay within 30 minutes, the timeout will cancel the order!\n",
                "order number：" + resultBookingTicketHtmlReturnResult.getSequenceNo() + "\n",
                "document type：" + resultBookingTicketHtmlReturnResult.getPassengerIdTypeName() + "\n",
                "document number：" + resultBookingTicketHtmlReturnResult.getPassengerIdNo() + "\n",
                "passenger name：" + resultBookingTicketHtmlReturnResult.getPassengerName() + "\n",
                "coach number：" + resultBookingTicketHtmlReturnResult.getCoachName() + "\n",
                "seat number：" + resultBookingTicketHtmlReturnResult.getSeatName() + "\n",
                "seat type：" + resultBookingTicketHtmlReturnResult.getSeatTypeName() + "\n",
                "from station：" + resultBookingTicketHtmlReturnResult.getFromStationName() + "\n",
                "to station：" + resultBookingTicketHtmlReturnResult.getToStationName() + "\n",
                "train number：" + resultBookingTicketHtmlReturnResult.getStationTrainCode() + "\n",
                "tran date：" + resultBookingTicketHtmlReturnResult.getStartTrainDate() + "\n",
                "ticket price：" + resultBookingTicketHtmlReturnResult.getTicketPrice() + "\n",
                "ticket number：" + resultBookingTicketHtmlReturnResult.getTicketNo() + "\n",
                "ticket type：" + resultBookingTicketHtmlReturnResult.getTicketTypeName() + "\n",
                "Naonao provides technology support"
        };
        for(String element: content){
            resultStr.append(element);
        }
        return resultStr.toString();
    }

    /**
     * format date
     * example:
     *      input ---> 20190513
     *      output --> 2019-05-13
     * @param date  date
     * @return      date
     */
    private String formatDate(String date){
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        return year + "-" + month + "-" + day;
    }

    private BookingReturnResult failedBookingReturnResult(CloseableHttpClient session, String message){
        BookingReturnResult bookingReturnResult = new BookingReturnResult();
        bookingReturnResult.setStatus(false);
        bookingReturnResult.setSession(session);
        bookingReturnResult.setMessage(message);
        return bookingReturnResult;
    }

    private BookingReturnResult successBookingReturnResult(CloseableHttpClient session, String message){
        BookingReturnResult bookingReturnResult = new BookingReturnResult();
        bookingReturnResult.setStatus(true);
        bookingReturnResult.setSession(session);
        bookingReturnResult.setMessage(message);
        return bookingReturnResult;
    }

    private BookingReturnResult successBookingReturnResult(CloseableHttpClient session,
                                                           String message,
                                                           String bookingResultString,
                                                           ResultBookingTicketHtmlReturnResult resultBookingTicketHtmlReturnResult){
        BookingReturnResult bookingReturnResult = new BookingReturnResult();
        bookingReturnResult.setStatus(true);
        bookingReturnResult.setSession(session);
        bookingReturnResult.setMessage(message);
        bookingReturnResult.setBookingResultString(bookingResultString);
        bookingReturnResult.setBookingResultObject(resultBookingTicketHtmlReturnResult);
        return bookingReturnResult;
    }

}
