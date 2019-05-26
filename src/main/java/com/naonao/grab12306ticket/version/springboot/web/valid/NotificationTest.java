package com.naonao.grab12306ticket.version.springboot.web.valid;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.springboot.entity.response.GeneralResponse;
import com.naonao.grab12306ticket.version.springboot.service.notification.Notification;
import com.naonao.grab12306ticket.version.springboot.web.base.AbstractValid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-25 18:34
 **/
@RestController
@RequestMapping(value = "${url.prefix}" + "valid")
public class NotificationTest extends AbstractValid {


    /**
     * check if the email can receive message.
     * json format:
     * {
     *     "receiverEmail": ""
     * }
     * @param inputData     json data
     * @return              generalResponse
     */
    @PostMapping("email")
    public GeneralResponse email(@RequestBody String inputData){
        JSONObject jsonObject = JSONObject.parseObject(inputData);
        String receiverEmail = jsonObject.getString("receiverEmail");
        // check email format
        String[] chars = receiverEmail.split("");
        if (!Arrays.asList(chars).contains(EMAIL_CHAR)){
            return generalResponse(false, EMAIL_FORMAT_ERROR);
        }
        Boolean result = new Notification().sendEmailTest(receiverEmail);
        if (result){
            return generalResponse(true, AN_EMAIL_HAS_BEEN_SENT_TO_YOUR_EMAIL_PLEASE_LOGIN_TO_VIEW);
        }
        return generalResponse(false, SEND_EMAIL_FAILED);
    }

    /**
     * check if the phone can receive message.
     * json format:
     * {
     *     "receiverPhone": ""
     * }
     * @param inputData     json data
     * @return              generalResponse
     */
    @PostMapping("sms")
    public GeneralResponse sms(@RequestBody String inputData){
        JSONObject jsonObject = JSONObject.parseObject(inputData);
        String receiverPhone = jsonObject.getString("receiverPhone");
        // check phone format
        if (!StringUtils.isNumeric(receiverPhone) || "".equals(receiverPhone)){
            return generalResponse(false, PHONE_EMAIL_FAILED);
        }
        Boolean result = new Notification().sendSmsTest(receiverPhone);
        if (result){
            return generalResponse(true, A_SMS_HAS_BEEN_SENT_TO_YOUR_PHONE_PLEASE_CHECK);
        }
        return generalResponse(false, SEND_SMS_FAILED);
    }

    /**
     * check if the phone can receive message.
     * json format:
     * {
     *     "receiverPhone": ""
     * }
     * @param inputData     json data
     * @return              generalResponse
     */
    @PostMapping("phone")
    public GeneralResponse phone(@RequestBody String inputData){
        JSONObject jsonObject = JSONObject.parseObject(inputData);
        String receiverPhone = jsonObject.getString("receiverPhone");
        // check phone format
        if (!StringUtils.isNumeric(receiverPhone) || "".equals(receiverPhone)){
            return generalResponse(false, PHONE_EMAIL_FAILED);
        }
        Boolean result = new Notification().sendPhoneTest(receiverPhone);
        if (result){
            return generalResponse(true, REQUEST_SUCCESS_PLEASE_PAY_ATTENTION_TO_THE_CALL);
        }
        return generalResponse(false, SEND_PHONE_FAILED);
    }

    private GeneralResponse generalResponse(Boolean status, String message){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setStatus(status);
        generalResponse.setHttpStatus(HTTP_SUCCESS);
        generalResponse.setMessage(message);
        return generalResponse;
    }
}
