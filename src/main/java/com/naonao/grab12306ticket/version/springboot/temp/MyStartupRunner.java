package com.naonao.grab12306ticket.version.springboot.temp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @program: grab12306ticket
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-14 16:05
 **/
@Component
public class MyStartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Project start running ......");
    }

}
