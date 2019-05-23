package com.naonao.grab12306ticket.version.springboot.entity.response;

import com.naonao.grab12306ticket.version.springboot.entity.IResponseInterface;
import lombok.Data;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-15 04:02
 **/
@Data
public class DeleteResponse implements IResponseInterface {

    private Boolean status;
    private Integer httpStatus;
    private String message;

}
