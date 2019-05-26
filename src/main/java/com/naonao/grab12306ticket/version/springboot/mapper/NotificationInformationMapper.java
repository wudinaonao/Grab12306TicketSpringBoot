package com.naonao.grab12306ticket.version.springboot.mapper;


import com.naonao.grab12306ticket.version.springboot.entity.database.NotificationInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:41
 **/
@Mapper
@Component
@Scope("prototype")
public interface NotificationInformationMapper {

    NotificationInformationEntity getNotificationInformationById(Integer id);
    NotificationInformationEntity getNotificationInformationByHash(String hash);

    void  insert(NotificationInformationEntity notificationInformationTable);
    void  delete(NotificationInformationEntity notificationInformationTable);
    void  update(NotificationInformationEntity notificationInformationTable);

}
