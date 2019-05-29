package com.naonao.grab12306ticket.version.springboot.interceptor;

import com.naonao.grab12306ticket.version.springboot.entity.response.GeneralResponse;
import com.naonao.grab12306ticket.version.springboot.interceptor.base.AbstractInterceptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-29 18:25
 **/
@ControllerAdvice
@ResponseBody
public class HTTPExceptionHandler  extends AbstractInterceptor {

    /**
     * not found exception
     * @return  GeneralResponse
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    public GeneralResponse notFound(){
        return failedGeneralResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    /**
     * requets method not allowed exception
     * @return  GeneralResponse
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public GeneralResponse methodNotAllowed(){
        return failedGeneralResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }


    /**
     * if request need a body but not have it, then return 400 code
     * @return  GeneralResponse
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public GeneralResponse notHaveBody(){
        return failedGeneralResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

}
