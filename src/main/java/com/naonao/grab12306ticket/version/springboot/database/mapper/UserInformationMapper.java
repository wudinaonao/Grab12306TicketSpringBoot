package com.naonao.grab12306ticket.version.springboot.database.mapper;

import com.naonao.grab12306ticket.version.springboot.database.entity.UserInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:33
 **/
@Mapper
@Component
public interface UserInformationMapper {


    UserInformationEntity getUsernameAndPasswordById(Integer id);
    UserInformationEntity getUsernameAndPasswordByHash(String hash);
    UserInformationEntity getPasswordByUsername(String username);

    void  insert(UserInformationEntity userInformationTable);
    void  delete(UserInformationEntity userInformationTable);
    void  update(UserInformationEntity userInformationTable);


}
