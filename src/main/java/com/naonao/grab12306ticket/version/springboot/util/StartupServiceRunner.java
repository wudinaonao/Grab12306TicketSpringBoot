package com.naonao.grab12306ticket.version.springboot.util;

import com.naonao.grab12306ticket.version.springboot.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.springboot.exception.ConfigurationNotCompletedException;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.Scheduler;
import com.naonao.grab12306ticket.version.springboot.service.tools.GeneralTools;
import com.naonao.grab12306ticket.version.springboot.util.base.AbstractUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @program: SpringBoot
 * @description: follow the background task started by Spring boot
 * @author: Wen lyuzhao
 * @create: 2019-05-23 16:24
 **/
@Component
@Slf4j
public class StartupServiceRunner extends AbstractUtil implements CommandLineRunner {

    private Configuration configuration;

    @Autowired
    private Scheduler scheduler;


    public StartupServiceRunner(){
        configuration = GeneralTools.getConfiguration();
    }

    /**
     * started background task
     * @param args          arguments
     * @throws Exception    Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // check configuration
        checkConfiguration();
        // grab ticket core
        if (configuration.getSetting().getGrabTicketCode()){
            onGrabTicketCore();
        }
    }

    /**
     * check if the configuration file is valid.
     * if not completed, then throw an exception.
     * @throws ConfigurationNotCompletedException   ConfigurationNotCompletedException
     */
    private void checkConfiguration() throws ConfigurationNotCompletedException {
        log.info(CHECK_CONFIGURATION);
        GeneralTools.checkConfiguration();
        log.info(INSPECTION_PASSED);
    }

    /**
     * started grab ticket core
     */
    private void onGrabTicketCore(){
        // grab ticket core
        scheduler.start();
        log.info(START_RUNNING_GRAB_TICKET_CORE);
    }

}
