package com.naonao.grab12306ticket.version.springboot.entity.yml;

import com.naonao.grab12306ticket.version.springboot.entity.yml.setting.Encryption;
import lombok.Data;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-26 12:14
 **/
@Data
public class Setting {

    private Boolean grabTicketCode;
    private Encryption encryption;
}
