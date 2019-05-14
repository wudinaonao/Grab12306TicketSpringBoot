package com.naonao.grab12306ticket.version.springboot;

import com.naonao.grab12306ticket.version.springboot.Grab12306TicketApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-10 18:31
 **/
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Grab12306TicketApplication.class);
    }

    public static void main(String[] args) {

    }

}
