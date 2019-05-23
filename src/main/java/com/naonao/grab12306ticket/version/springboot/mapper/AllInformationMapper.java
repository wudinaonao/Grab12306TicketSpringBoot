package com.naonao.grab12306ticket.version.springboot.mapper;

import com.naonao.grab12306ticket.version.springboot.entity.database.AllInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-13 21:47
 **/
@Mapper
@Component
@Scope("prototype")
public interface AllInformationMapper {

    AllInformationEntity getAllInformationEntityById(Integer id);
    AllInformationEntity getAllInformationEntityByHash(String hash);
    List<AllInformationEntity> getAllInformationEntityByAll();

    void deleteAllByHash(String hash);
}
