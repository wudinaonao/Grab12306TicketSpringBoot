package com.naonao.grab12306ticket.version.springboot.entity.request;

import lombok.Data;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-28 23:22
 **/
@Data
public class NotificationTestRequest {
    private String receiverEmail;
    private String receiverPhone;
}
