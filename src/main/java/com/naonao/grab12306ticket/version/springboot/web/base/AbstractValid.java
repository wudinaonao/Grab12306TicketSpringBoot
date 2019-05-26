package com.naonao.grab12306ticket.version.springboot.web.base;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-25 18:49
 **/
public class AbstractValid extends AbstractWeb{


    protected static final String EMAIL_CHAR = "@";

    protected static final String EMAIL_FORMAT_ERROR = "email format error.";
    protected static final String AN_EMAIL_HAS_BEEN_SENT_TO_YOUR_EMAIL_PLEASE_LOGIN_TO_VIEW = "an email has been sent to your email, please login to view.";
    protected static final String SEND_EMAIL_FAILED = "send email failed.";

    protected static final String PHONE_EMAIL_FAILED = "phone format error.";
    protected static final String A_SMS_HAS_BEEN_SENT_TO_YOUR_PHONE_PLEASE_CHECK = "a SMS has been sent to your phone, please check.";
    protected static final String SEND_SMS_FAILED = "send SMS failed.";

    protected static final String REQUEST_SUCCESS_PLEASE_PAY_ATTENTION_TO_THE_CALL = "request success, please pay attention to the call.";
    protected static final String SEND_PHONE_FAILED = "send phone failed.";

}
