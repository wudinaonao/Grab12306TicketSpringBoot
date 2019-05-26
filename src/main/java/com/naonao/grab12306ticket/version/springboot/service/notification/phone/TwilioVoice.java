package com.naonao.grab12306ticket.version.springboot.service.notification.phone;



import com.naonao.grab12306ticket.version.springboot.service.base.AbstractNotification;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Twilio voice interface
 *
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 20:21
 **/
@Slf4j
public class TwilioVoice extends AbstractNotification {

    

    private String accountSid;
    private String authToken;

    public TwilioVoice(String accountSid, String authToken){
        this.accountSid = accountSid;
        this.authToken = authToken;
    }


    /**
     *      Twilio voice interface
     *
     * @param from              from
     * @param tos               tos
     * @param configPath        configPath --> soundUrl
     * @return                  true, false
     */
    public Boolean sendPhoneVoice(String from, String[] tos, String configPath){
        Twilio.init(accountSid, authToken);
        com.twilio.rest.api.v2010.account.Call call;
        for (String to:tos) {
            try {
                call = com.twilio.rest.api.v2010.account.Call.creator(
                        new PhoneNumber(formatNumberOfPhone(to)),
                        new PhoneNumber(formatNumberOfPhone(from)),
                        new URI(configPath)
                ).create();
            } catch (URISyntaxException e) {
                log.error(e.getMessage());
                return false;
            }
            if (!QUEUED.equals(call.getStatus().name())){
                return false;
            }
        }
        return true;
    }

    /**
     *      Twilio voice interface
     *
     * @param from              from
     * @param to                to
     * @param configPath        configPath --> soundUrl
     * @return                  true, false
     */
    public Boolean sendPhoneVoice(String from, String to, String configPath){
        Twilio.init(accountSid, authToken);
        com.twilio.rest.api.v2010.account.Call call;
        try {
            call = com.twilio.rest.api.v2010.account.Call.creator(
                    new PhoneNumber(formatNumberOfPhone(to)),
                    new PhoneNumber(formatNumberOfPhone(from)),
                    new URI(configPath)
            ).create();
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
            return false;
        }
        if (!QUEUED.equals(call.getStatus().name())){
            return false;
        }
        return true;
    }

    private String formatNumberOfPhone(String number){
        if (!TWILIO_NUMBER_PREFIX.equals(number.substring(0, 1))){
            return TWILIO_NUMBER_PREFIX + number;
        }
        return number;
    }

}
