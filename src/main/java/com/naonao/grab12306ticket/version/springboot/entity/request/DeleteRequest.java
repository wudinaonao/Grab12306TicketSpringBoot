package com.naonao.grab12306ticket.version.springboot.entity.request;

import com.naonao.grab12306ticket.version.springboot.entity.IRequestInterface;
import lombok.Data;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-28 23:09
 **/
@Data
public class DeleteRequest implements IRequestInterface {
    private String hash;
}
