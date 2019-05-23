package com.naonao.grab12306ticket.version.springboot.entity;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-15 02:43
 **/
public interface IResponseInterface {

    public Boolean getStatus();
    public void setStatus(Boolean status);

    public Integer getHttpStatus();
    public void setHttpStatus(Integer httpStatus);

    public String getMessage();
    public void setMessage(String message);
}
