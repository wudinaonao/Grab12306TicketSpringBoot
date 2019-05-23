package com.naonao.grab12306ticket.version.springboot.entity.database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-22 14:00
 **/
@ToString
@Getter
@Setter
public class NotificationInformationEntity {

    private Integer id;
    private String receiverEmail;
    private String sendEmail;
    private String sendEmailHost;
    private String sendEmailPort;
    private String sendEmailUsername;
    private String sendEmailPassword;
    private String receiverPhone;
    private String notificationMode;
    private String hash;


}
