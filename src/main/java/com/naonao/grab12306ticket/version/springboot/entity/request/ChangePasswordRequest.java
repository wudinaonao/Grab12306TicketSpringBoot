package com.naonao.grab12306ticket.version.springboot.entity.request;

import lombok.Data;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-29 17:51
 **/
@Data
public class ChangePasswordRequest {
    private String username12306;
    private String oldPassword12306;
    private String newPassword12306;
}
