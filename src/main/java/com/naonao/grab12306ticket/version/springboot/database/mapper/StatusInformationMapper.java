package com.naonao.grab12306ticket.version.springboot.database.mapper;


import com.naonao.grab12306ticket.version.springboot.database.entity.StatusInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 23:43
 **/
@Mapper
@Component
public interface StatusInformationMapper {

    StatusInformationEntity getStatusInformationById(Integer id);
    StatusInformationEntity getStatusInformationByHash(String hash);
    List<StatusInformationEntity> getStatusInformationListByStatus(String status);
    List<StatusInformationEntity> getStatusInformationTableListByUnfinished();

    void  insert(StatusInformationEntity statusInformationTable);
    void  delete(StatusInformationEntity statusInformationTable);
    void  update(StatusInformationEntity statusInformationTable);

}
