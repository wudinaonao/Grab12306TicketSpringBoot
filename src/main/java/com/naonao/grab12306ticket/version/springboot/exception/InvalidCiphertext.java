package com.naonao.grab12306ticket.version.springboot.exception;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-29 18:08
 **/
public class InvalidCiphertext extends RuntimeException {

    public InvalidCiphertext(){

    }

    public InvalidCiphertext(String message){
        super(message);
    }

}
