package com.naonao.grab12306ticket.version.springboot.mapper;

import com.naonao.grab12306ticket.version.springboot.entity.database.UserInformationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:33
 **/
@Mapper
@Component
@Scope("prototype")
public interface UserInformationMapper {


    UserInformationEntity getUsernameAndPasswordById(Integer id);
    UserInformationEntity getUsernameAndPasswordByHash(String hash);
    UserInformationEntity getPasswordByUsername(String username);
    List<UserInformationEntity> getEntityByUsernameAndPassword(@Param("username12306") String username12306,
                                                               @Param("password12306") String password12306);
    List<UserInformationEntity> getEntityByUsername(@Param("username12306") String username12306);

    void  insert(UserInformationEntity userInformationTable);
    void  delete(UserInformationEntity userInformationTable);
    void  update(UserInformationEntity userInformationTable);



}
