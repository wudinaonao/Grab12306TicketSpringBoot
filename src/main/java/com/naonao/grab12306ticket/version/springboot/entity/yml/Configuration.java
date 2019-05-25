package com.naonao.grab12306ticket.version.springboot.entity.yml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-24 22:12
 **/
@Getter
@Setter
@ToString
@Component
public class Configuration {

    private Platform platform;
    private Notification notification;

}
