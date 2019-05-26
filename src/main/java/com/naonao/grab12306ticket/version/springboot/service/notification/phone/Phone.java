package com.naonao.grab12306ticket.version.springboot.service.notification.phone;


import com.naonao.grab12306ticket.version.springboot.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.springboot.service.base.AbstractNotification;
import com.naonao.grab12306ticket.version.springboot.service.tools.GeneralTools;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 22:52
 **/
@Slf4j
public class Phone extends AbstractNotification {

    

    private Configuration configuration;

    public Phone(){
        configuration = GeneralTools.getConfiguration();
    }

    public TwilioVoice twilio(){
        if (!checkConfiguration()){
            return null;
        }
        String accountSid = configuration.getPlatform().getConfig().getTwilio().getAccountSid().trim();
        String authToken = configuration.getPlatform().getConfig().getTwilio().getAuthToken().trim();
        if (!"".equals(accountSid) && !"".equals(authToken)){
            return new TwilioVoice(accountSid, authToken);
        }
        return null;
    }

    public YunzhixinVoice yunzhixin(){
        if (!checkConfiguration()){
            return null;
        }
        String appCode = configuration.getPlatform().getConfig().getYunzhixin().getAppCode().trim();
        if (!"".equals(appCode)){
            return new YunzhixinVoice(appCode);
        }
        return null;
    }

    private Boolean checkConfiguration(){
        if (configuration == null){
            log.error(READ_CONFIG_FAILED);
            return false;
        }
        return true;
    }

}
