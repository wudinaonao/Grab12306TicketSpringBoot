package com.naonao.grab12306ticket.version.springboot.entity.response;

import com.naonao.grab12306ticket.version.springboot.entity.IResponseInterface;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-14 22:40
 **/
@Component
@Data
public class QueryResponse implements IResponseInterface {

    private Boolean status;
    private Integer httpStatus;
    private String message;

    private String taskStatus;
    private List<?> result;

}
