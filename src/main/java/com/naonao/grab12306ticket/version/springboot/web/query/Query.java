package com.naonao.grab12306ticket.version.springboot.web.query;

import com.naonao.grab12306ticket.version.springboot.database.entity.AllInformationEntity;
import com.naonao.grab12306ticket.version.springboot.database.mapper.AllInformationMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-14 00:15
 **/
@RestController
@RequestMapping(value = "query", method = RequestMethod.GET)
public class Query {

    @Autowired
    private AllInformationMapper allInformationMapper;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<AllInformationEntity> getAllInformationEntityAll(){
        return allInformationMapper.getAllInformationEntityByAll();
    }

}
