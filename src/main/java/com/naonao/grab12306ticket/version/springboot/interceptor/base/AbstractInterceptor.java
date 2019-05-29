package com.naonao.grab12306ticket.version.springboot.interceptor.base;

import com.naonao.grab12306ticket.version.springboot.base.AbstractSpringBoot;
import com.naonao.grab12306ticket.version.springboot.entity.response.GeneralResponse;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-26 13:28
 **/
public class AbstractInterceptor extends AbstractSpringBoot {

    protected static final String INVALID_CIPHER_TEXT = "invalid cipher text.";

    protected GeneralResponse failedGeneralResponse(Integer httpStatus, String message){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setStatus(false);
        generalResponse.setHttpStatus(httpStatus);
        generalResponse.setMessage(message);
        return generalResponse;
    }
}
