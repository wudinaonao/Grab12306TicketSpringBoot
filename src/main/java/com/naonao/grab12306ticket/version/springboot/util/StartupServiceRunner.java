package com.naonao.grab12306ticket.version.springboot.util;

import com.naonao.grab12306ticket.version.springboot.service.scheduler.Scheduler;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-23 16:24
 **/
@Component
@Log4j
public class StartupServiceRunner implements CommandLineRunner {

    @Autowired
    private Scheduler scheduler;

    @Override
    public void run(String... args) throws Exception {
        scheduler.start();
        log.info("start running grab ticket core ......");
    }

}
