package com.naonao.grab12306ticket.version.springboot.entity.response;

import com.naonao.grab12306ticket.version.springboot.entity.IResponseInterface;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-15 13:16
 **/
@Component
@Data
public class GeneralResponse implements IResponseInterface {

    private Boolean status;
    private Integer httpStatus;
    private String message;

    public GeneralResponse(){

    }

    public GeneralResponse(Boolean status,
                           Integer httpStatus,
                           String message){
        this.status = status;
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
