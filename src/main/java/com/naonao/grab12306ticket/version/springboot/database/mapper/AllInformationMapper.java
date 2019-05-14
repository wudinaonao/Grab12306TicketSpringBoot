package com.naonao.grab12306ticket.version.springboot.database.mapper;

import com.naonao.grab12306ticket.version.springboot.database.entity.AllInformationEntity;
import org.apache.ibatis.annotations.Mapper;
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
public interface AllInformationMapper {

    AllInformationEntity getAllInformationEntityById(Integer id);
    AllInformationEntity getAllInformationEntityByHash(String hash);
    List<AllInformationEntity> getAllInformationEntityByAll();
}
