package com.naonao.grab12306ticket.version.springboot.web.add;

import com.naonao.grab12306ticket.version.springboot.database.entity.AllInformationEntity;
import com.naonao.grab12306ticket.version.springboot.database.entity.UserInformationEntity;
import com.naonao.grab12306ticket.version.springboot.database.mapper.AllInformationMapper;
import com.naonao.grab12306ticket.version.springboot.database.mapper.GrabTicketInformationMapper;
import com.naonao.grab12306ticket.version.springboot.database.mapper.UserInformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-13 21:29
 **/
@RestController
@RequestMapping("add")
public class Add {

    @Autowired
    private UserInformationMapper userInformationMapper;

    @Autowired
    private AllInformationMapper allInformationMapper;

    @RequestMapping(value = "")
    public AllInformationEntity allInformationEntity(){
        return allInformationMapper.getAllInformationEntityByHash("A82B819EDC4E56762D79314C3624F4CA");
    }
}
