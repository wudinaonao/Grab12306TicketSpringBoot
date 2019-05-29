package com.naonao.grab12306ticket.version.springboot.interceptor;

import com.naonao.grab12306ticket.version.springboot.entity.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @program: SpringBoot
 * @description: global exception handle
 * @author: Wen lyuzhao
 * @create: 2019-05-26 13:13
 **/
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {


    /**
     * not found exception
     * @return  GeneralResponse
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    public GeneralResponse notFound(){
        return generalResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    /**
     * requets method not allowed exception
     * @return  GeneralResponse
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public GeneralResponse methodNotAllowed(){
        return generalResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }


    /**
     * if request need a body but not have it, then return 400 code
     * @return  GeneralResponse
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public GeneralResponse notHaveBody(){
        return generalResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    private GeneralResponse generalResponse(Integer httpStatus, String message){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setStatus(false);
        generalResponse.setHttpStatus(httpStatus);
        generalResponse.setMessage(message);
        return generalResponse;
    }
}
