package com.naonao.grab12306ticket.version.springboot.resultclass.service.preprocess;

import com.naonao.grab12306ticket.version.springboot.resultclass.IGeneralReturnResult;
import lombok.Data;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-15 13:30
 **/
@Data
public class CheckInformationReturnResult implements IGeneralReturnResult {

    private Boolean status;
    private String message;

}
