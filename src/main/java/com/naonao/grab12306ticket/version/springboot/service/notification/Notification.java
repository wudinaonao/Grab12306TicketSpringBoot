package com.naonao.grab12306ticket.version.springboot.service.notification;


import com.naonao.grab12306ticket.version.springboot.constants.NotificationMethodName;
import com.naonao.grab12306ticket.version.springboot.entity.database.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.springboot.exception.ConfigurationNotCompletedException;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.booking.BookingReturnResult;
import com.naonao.grab12306ticket.version.springboot.service.base.AbstractNotification;
import com.naonao.grab12306ticket.version.springboot.service.notification.email.Email;
import com.naonao.grab12306ticket.version.springboot.service.notification.phone.Phone;
import com.naonao.grab12306ticket.version.springboot.service.notification.sms.SMS;
import com.naonao.grab12306ticket.version.springboot.service.tools.GeneralTools;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 00:28
 **/
@Slf4j
public class Notification extends AbstractNotification {


    private Configuration configuration;

    /**
     * initialization configuration file
     */
    public Notification(){
        configuration = GeneralTools.getConfiguration();
    }

    /**
     * Select the delivery method according to the notification method
     * @param bookingReturnResult       bookingReturnResult
     * @param notificationInformationEntity    notificationInformationEntity
     * @return                          Boolean
     */
    public Boolean sendNotification(BookingReturnResult bookingReturnResult, NotificationInformationEntity notificationInformationEntity) throws Exception{
        // here is send notification method,
        // get notification mode from grabTicketInformationTable
        // notification mode format ---> email, phone, sms

        // get user set notification mode list
        List<NotificationMethodName> notifyMethodNameList = getNotificationMethodNameList(notificationInformationEntity.getNotificationMode());
        if (notifyMethodNameList == null){
            log.error(NOT_FOUND_ELIGIBLE_NOTIFICATION_MODE);
            return false;
        }
        // Maybe have multi result
        List<Boolean> resultList = new ArrayList<>();
        for (NotificationMethodName notifyMethodName: notifyMethodNameList){
            resultList.add(sendNotification(notifyMethodName, bookingReturnResult, notificationInformationEntity));
        }
        return handleResult(resultList);
    }


    /**
     * send email test by default set
     * @param receiverEmail     receiverEmail
     * @return                  Boolean
     */
    public Boolean sendEmailTest(String receiverEmail){
        Email email = new Email(receiverEmail);
        String subject = configuration.getNotification().getConfig().getEmail().getDefaultTitle();
        String text = configuration.getNotification().getConfig().getEmail().getDefaultContent();
        return email.sendEmailText(subject, text);
    }

    /**
     * send sms test by default set
     * @param receiverPhone     receiverPhone
     * @return                  Boolean
     */
    public Boolean sendSmsTest(String receiverPhone) throws ConfigurationNotCompletedException{
        String text = configuration.getNotification().getConfig().getSms().getDefaultContent();
        String fromPhoneNumber;
        String title;
        switch (configuration.getNotification().getConfig().getSms().getInterfaceName()){
            case TWILIO:
                fromPhoneNumber = configuration.getPlatform().getConfig().getTwilio().getFromPhoneNumber();
                return new SMS().twilio().sendSMS(fromPhoneNumber, receiverPhone, text);
            case NEXMO:
                title = configuration.getNotification().getConfig().getSms().getTitle();
                return new SMS().nexmo().sendSMS(title, receiverPhone, text);
            default:
                throw new ConfigurationNotCompletedException(PLEASE_SETTING_NOTIFY_SMS_INTERFACE_NAME);
        }
    }

    /**
     * send phone test by default set
     * @param receiverPhone     receiverPhone
     * @return                  Boolean
     */
    public Boolean sendPhoneTest(String receiverPhone) throws ConfigurationNotCompletedException{
        String fromPhoneNumber;
        String configPath;
        switch (configuration.getNotification().getConfig().getPhone().getInterfaceName()){
            case TWILIO:
                fromPhoneNumber = configuration.getPlatform().getConfig().getTwilio().getFromPhoneNumber();
                configPath = configuration.getPlatform().getConfig().getTwilio().getDefaultVoiceUrl();
                return new Phone().twilio().sendPhoneVoice(fromPhoneNumber, receiverPhone, configPath);
            case YUNZHIXIN:
                String templateId = configuration.getPlatform().getConfig().getYunzhixin().getTemplateId();
                return new Phone().yunzhixin().sendPhoneVoice(receiverPhone, templateId);
            default:
                throw new ConfigurationNotCompletedException(PLEASE_SETTING_NOTIFY_PHONE_INTERFACE_NAME);
        }
    }

    /**
     * send notification
     * if add a new method later, just override this method
     * @param notifyMethodName  notification method name
     * @return                  Boolean
     */
    private Boolean sendNotification(NotificationMethodName notifyMethodName,
                                     BookingReturnResult bookingReturnResult,
                                     NotificationInformationEntity notificationInformationEntity) throws Exception{
        if (notifyMethodName == NotificationMethodName.EMAIL){
            return email(bookingReturnResult, notificationInformationEntity);
        }
        if (notifyMethodName == NotificationMethodName.PHONE){
            return phone(notificationInformationEntity);
        }
        if (notifyMethodName == NotificationMethodName.SMS){
            return sms(notificationInformationEntity);
        }
        return false;
    }

    /**
     * send email
     * @param bookingReturnResult       bookingReturnResult
     * @param notificationInformationEntity    notificationInformationEntity
     * @return                          Boolean
     */
    private Boolean email(BookingReturnResult bookingReturnResult, NotificationInformationEntity notificationInformationEntity){
        String subject = String.format(
                "12306 grab ticket notification --- from: %s to: %s",
                bookingReturnResult.getBookingResultObject().getFromStationName(),
                bookingReturnResult.getBookingResultObject().getToStationName()
        );
        // String subject = "12306 notification test";
        String text = bookingReturnResult.getBookingResultString();
        Email email = new Email(notificationInformationEntity);
        return email.sendEmailText(subject, text);
    }


    /**
     * call phone
     * @param notificationInformationEntity     notificationInformationEntity
     * @return                                  Boolean
     */
    private Boolean phone(NotificationInformationEntity notificationInformationEntity) throws Exception{
        String fromPhoneNumber;
        String configPath;
        String to = notificationInformationEntity.getReceiverPhone().trim();
        switch (configuration.getNotification().getConfig().getPhone().getInterfaceName()){
            case TWILIO:
                fromPhoneNumber = configuration.getPlatform().getConfig().getTwilio().getFromPhoneNumber();
                configPath = configuration.getPlatform().getConfig().getTwilio().getVoiceUrl();
                return new Phone().twilio().sendPhoneVoice(fromPhoneNumber, to, configPath);
            case YUNZHIXIN:
                String templateId = configuration.getPlatform().getConfig().getYunzhixin().getTemplateId();
                return new Phone().yunzhixin().sendPhoneVoice(to, templateId);
            default:
                throw new Exception(PLEASE_SETTING_NOTIFY_PHONE_INTERFACE_NAME);
        }
    }



    /**
     * send sms
     * @param notificationInformationEntity    notificationInformationEntity
     * @return                                 Boolean
     */
    private Boolean sms(NotificationInformationEntity notificationInformationEntity) throws Exception{
        String text = configuration.getNotification().getConfig().getSms().getContent();
        String to = notificationInformationEntity.getReceiverPhone().trim();
        String fromPhoneNumber;
        String title;
        switch (configuration.getNotification().getConfig().getSms().getInterfaceName()){
            case TWILIO:
                fromPhoneNumber = configuration.getPlatform().getConfig().getTwilio().getFromPhoneNumber();
                return new SMS().twilio().sendSMS(fromPhoneNumber, to, text);
            case NEXMO:
                title = configuration.getNotification().getConfig().getSms().getTitle();
                return new SMS().nexmo().sendSMS(title, to, text);
            default:
                throw new Exception(PLEASE_SETTING_NOTIFY_SMS_INTERFACE_NAME);
        }
    }


    /**
     * get notification mode list by String
     * example:
     *          String ---> email, phone, sms
     *          List   ---> ["email", "phone", "sms']
     * list element is a enum type, reference NotificationMethodName class
     * @param notifyModeString      notifyModeString
     * @return                      List<NotificationMethodName>
     */
    private List<NotificationMethodName> getNotificationMethodNameList(String notifyModeString){
        String[] notifyModeArray = notifyModeString.split(",");
        List<NotificationMethodName> notifyMethodNameList = new ArrayList<>();
        for (String notifyMode: notifyModeArray){
            notifyMode = notifyMode.trim().toUpperCase();
            try{
                notifyMethodNameList.add(NotificationMethodName.valueOf(notifyMode));
            }catch (IllegalArgumentException e){
                continue;
            }
        }
        if (notifyMethodNameList.size() > 0){
            return notifyMethodNameList;
        }
        return null;
    }

    /**
     * handle notification result
     * because maybe have many notification mode, so may have many result.
     * if there are multiple results, traverse each result to determine if
     * it is true, false or null.
     * if have false or null, then return false.
     * @param resultList    resultList
     * @return              Boolean
     */
    private Boolean handleResult(List<Boolean> resultList){
        // create new result list save not null result
        List<Boolean> newResultList = new ArrayList<>();
        for (Boolean result: resultList){
            if (result != null){
                newResultList.add(result);
            }
        }
        // if result list all is null return false
        if (newResultList.size() <= 0 ){
            return false;
        }
        // compute boolean value
        Boolean returnResult = true;
        for (Boolean result: newResultList){
            returnResult = returnResult && result;
        }
        return returnResult;
    }

}
