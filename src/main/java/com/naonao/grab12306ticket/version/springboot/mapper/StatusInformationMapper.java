package com.naonao.grab12306ticket.version.springboot.mapper;


import com.naonao.grab12306ticket.version.springboot.entity.database.StatusInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Scope;
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
@Scope("prototype")
public interface StatusInformationMapper {

    StatusInformationEntity getStatusInformationById(Integer id);
    StatusInformationEntity getStatusInformationByHash(String hash);
    List<StatusInformationEntity> getStatusInformationListByStatus(String status);
    List<StatusInformationEntity> getStatusInformationTableListByUnfinished();
    StatusInformationEntity getStatusInformationByHashAndStatus(@Param("hash") String hash,
                                                                @Param("status") String status);
    StatusInformationEntity getStatusInformationTableListBySuccess(String hash);
    StatusInformationEntity getStatusInformationTableListByFailed(String hash);

    void  insert(StatusInformationEntity statusInformationTable);
    void  delete(StatusInformationEntity statusInformationTable);
    void  update(StatusInformationEntity statusInformationTable);

}
