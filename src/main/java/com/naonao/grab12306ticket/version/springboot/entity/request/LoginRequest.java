package com.naonao.grab12306ticket.version.springboot.entity.request;

import lombok.Data;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-28 23:20
 **/
@Data
public class LoginRequest {

    private String username12306;
    private String password12306;

}
