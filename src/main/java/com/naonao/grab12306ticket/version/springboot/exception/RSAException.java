package com.naonao.grab12306ticket.version.springboot.exception;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-27 23:13
 **/
public class RSAException extends RuntimeException{

    public RSAException(){

    }

    public RSAException(String message){
        super(message);
    }


}
