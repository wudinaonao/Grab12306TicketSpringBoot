package com.naonao.grab12306ticket.version.springboot.exception;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-26 13:45
 **/
public class ConfigurationNotCompletedException extends RuntimeException{

    public ConfigurationNotCompletedException(){

    }

    public ConfigurationNotCompletedException(String message){
        super(message);
    }

}
