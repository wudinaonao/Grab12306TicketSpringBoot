package com.naonao.grab12306ticket.version.springboot.service.notification.sms;

import com.naonao.grab12306ticket.version.springboot.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.springboot.service.base.AbstractNotification;
import com.naonao.grab12306ticket.version.springboot.service.tools.GeneralTools;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 22:52
 **/
@Slf4j
public class SMS extends AbstractNotification {



    private Configuration configuration;

    public SMS(){
        // this.interfaceName = interfaceName;
        configuration = GeneralTools.getConfiguration();
    }

    public NexmoSMS nexmo(){
        if (!checkConfiguration()){
            return null;
        }
        String apiKey = configuration.getPlatform().getConfig().getNexmo().getApiKey().trim();
        String apiSecret = configuration.getPlatform().getConfig().getNexmo().getApiSecret().trim();
        if (!"".equals(apiKey) && !"".equals(apiSecret)){
            return new NexmoSMS(apiKey, apiSecret);
        }
        return null;
    }

    public TwilioSMS twilio(){
        if (!checkConfiguration()){
            return null;
        }
        String accountSid = configuration.getPlatform().getConfig().getTwilio().getAccountSid().trim();
        String authToken = configuration.getPlatform().getConfig().getTwilio().getAuthToken().trim();
        if (!"".equals(accountSid) && !"".equals(authToken)){
            return new TwilioSMS(accountSid, authToken);
        }
        return null;
    }

    private Boolean checkConfiguration(){
        if (configuration == null){
            log.error(READ_CONFIGURE_FAILED);
            return false;
        }
        return true;
    }


}
