package com.naonao.grab12306ticket.version.springboot.interceptor;

import com.naonao.grab12306ticket.version.springboot.entity.response.GeneralResponse;
import com.naonao.grab12306ticket.version.springboot.exception.InvalidCiphertext;
import com.naonao.grab12306ticket.version.springboot.interceptor.base.AbstractInterceptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: SpringBoot
 * @description: global exception handle
 * @author: Wen lyuzhao
 * @create: 2019-05-26 13:13
 **/
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler extends AbstractInterceptor {


    /**
     * check if cipher text is invalid
     * @param invalidCiphertext     InvalidCiphertext
     * @return                      GeneralResponse
     */
    @ExceptionHandler({InvalidCiphertext.class})
    public GeneralResponse invalidCiphertext(InvalidCiphertext invalidCiphertext){
        return failedGeneralResponse(HttpStatus.OK.value(), invalidCiphertext.getMessage());
    }


}
