package com.naonao.grab12306ticket.version.springboot.entity.response;

import com.naonao.grab12306ticket.version.springboot.entity.IResponseInterface;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-15 02:47
 **/
@Component
@Data
public class LoginTestResponse implements IResponseInterface {

    private Boolean status;
    private Integer httpStatus;
    private String message;

    private String username12306;
    private String sessionId;

    // private String loginSuccessToken;
}
