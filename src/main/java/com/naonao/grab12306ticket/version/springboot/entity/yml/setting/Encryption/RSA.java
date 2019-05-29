package com.naonao.grab12306ticket.version.springboot.entity.yml.setting.encryption;

import lombok.Data;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-27 23:21
 **/
@Data
public class RSA {
    private String publicFilePath;
    private String privateFilePath;
}
