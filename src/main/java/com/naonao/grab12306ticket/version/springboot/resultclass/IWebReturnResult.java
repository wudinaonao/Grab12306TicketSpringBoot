package com.naonao.grab12306ticket.version.springboot.resultclass;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-22 14:09
 **/
public interface IWebReturnResult {

    Boolean getStatus();

    void setStatus(Boolean status);

    String getMessage();

    void setMessage(String message);

}
