package com.naonao.grab12306ticket.version.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-11 01:25
 **/
@SpringBootApplication
@EnableRedisHttpSession
// @ServletComponentScan
public class Grab12306TicketApplication {
    public static void main(String[] args) {
        SpringApplication.run(Grab12306TicketApplication.class, args);
    }
}
