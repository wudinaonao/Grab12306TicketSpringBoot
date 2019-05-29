package com.naonao.grab12306ticket.version.springboot.entity.request;

import com.naonao.grab12306ticket.version.springboot.entity.IRequestInterface;
import com.naonao.grab12306ticket.version.springboot.entity.database.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.NotificationInformationEntity;
import lombok.Data;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-28 23:11
 **/
@Data
public class InsertRequest implements IRequestInterface {
    private GrabTicketInformationEntity grabTicketInformation;
    private NotificationInformationEntity notificationInformation;
}
