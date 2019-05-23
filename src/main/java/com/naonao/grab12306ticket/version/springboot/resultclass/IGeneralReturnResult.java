package com.naonao.grab12306ticket.version.springboot.resultclass;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-22 14:10
 **/
public interface IGeneralReturnResult {

    Boolean getStatus();

    void setStatus(Boolean status);

    String getMessage();

    void setMessage(String message);


}
