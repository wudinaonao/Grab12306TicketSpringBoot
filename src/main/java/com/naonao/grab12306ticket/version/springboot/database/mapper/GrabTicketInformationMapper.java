package com.naonao.grab12306ticket.version.springboot.database.mapper;

import com.naonao.grab12306ticket.version.springboot.database.entity.GrabTicketInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:39
 **/
@Mapper
@Component
public interface GrabTicketInformationMapper {

    GrabTicketInformationEntity getGrabTicketInformationById(Integer id);
    GrabTicketInformationEntity getGrabTicketInformationByHash(String hash);

    void  insert(GrabTicketInformationEntity grabTicketInformationTable);
    void  delete(GrabTicketInformationEntity grabTicketInformationTable);
    void  update(GrabTicketInformationEntity grabTicketInformationTable);

}
