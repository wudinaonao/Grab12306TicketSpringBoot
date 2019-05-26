package com.naonao.grab12306ticket.version.springboot.interceptor;

import com.naonao.grab12306ticket.version.springboot.entity.response.GeneralResponse;
import com.naonao.grab12306ticket.version.springboot.interceptor.base.AbstractInterceptor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-26 13:13
 **/
@ControllerAdvice
public class GlobalExceptionHandler extends AbstractInterceptor {

    /**
     * global exception handle
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public GeneralResponse defaultErrorHandler(HttpServletRequest request, Exception exception) throws Exception {

        if (exception instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            return generalResponse(false, 400, NOT_FOUND);
        }
        return generalResponse(false, 500, SERVER_ERROR);
    }

    private GeneralResponse generalResponse(Boolean status, Integer httpStatus, String message){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setStatus(status);
        generalResponse.setHttpStatus(httpStatus);
        generalResponse.setMessage(message);
        return generalResponse;
    }
}
