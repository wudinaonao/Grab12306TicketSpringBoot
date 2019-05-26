package com.naonao.grab12306ticket.version.springboot.service.base;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-22 13:55
 **/
public class AbstractNotification extends AbstractService{


    /**
     * Email
     */
    protected static final String INITIALIZATION_EMAIL_SERVER_FAIED = "initialization email server failed";


    protected static final String NOT_FOUND_ELIGIBLE_NOTIFICATION_MODE = "not found eligible notification mode";


    /**
     * Phone
     */
    protected static final String NOT_FOUND_SEND_INTERFACE = "not found send interface";
    protected static final String QUEUED = "QUEUED";
    protected static final String SEND_SUCCESS_CODE = "00000";
    protected static final String TWILIO = "TWILIO";
    protected static final String YUNZHIXIN = "YUNZHIXIN";
    protected static final String TWILIO_NUMBER_PREFIX = "+";

    /**
     * SMS
     */
    protected static final String NEXMO = "NEXMO";
    protected static final String READ_CONFIGURE_FAILED = "read yml configure file failed, because is null";
    protected static final String OK = "OK";

    /**
     * Notification
     */
    protected static final String PLEASE_SETTING_NOTIFY_PHONE_INTERFACE_NAME = "please setting notify phone interface name";
    protected static final String PLEASE_SETTING_NOTIFY_SMS_INTERFACE_NAME = "please setting notify sms interface name";

}
