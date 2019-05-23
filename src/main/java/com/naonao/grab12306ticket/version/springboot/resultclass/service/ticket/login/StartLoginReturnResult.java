package com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.login;

import com.naonao.grab12306ticket.version.springboot.resultclass.ITicketReturnResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-04-30 22:59
 **/
@Getter
@Setter
public class StartLoginReturnResult implements ITicketReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;

    private String uamtk;

}
