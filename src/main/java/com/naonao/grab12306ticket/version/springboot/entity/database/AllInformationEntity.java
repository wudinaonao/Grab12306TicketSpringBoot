package com.naonao.grab12306ticket.version.springboot.entity.database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-13 21:45
 **/
@ToString
@Setter
@Getter
public class AllInformationEntity {

    private Integer id;
    private String hash;

    // private UserInformationEntity userInformationEntity;
    private GrabTicketInformationEntity grabTicketInformationEntity;
    private NotificationInformationEntity notificationInformationEntity;
    private StatusInformationEntity statusInformationEntity;

}
