package com.naonao.grab12306ticket.version.springboot.database.mapper;


import com.naonao.grab12306ticket.version.springboot.database.entity.NotifyInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:41
 **/
@Mapper
@Component
public interface NotifyInformationMapper {

    NotifyInformationEntity getNotifyInformationById(Integer id);
    NotifyInformationEntity getNotifyInformationByHash(String hash);

    void  insert(NotifyInformationEntity notifyInformationTable);
    void  delete(NotifyInformationEntity notifyInformationTable);
    void  update(NotifyInformationEntity notifyInformationTable);

}
