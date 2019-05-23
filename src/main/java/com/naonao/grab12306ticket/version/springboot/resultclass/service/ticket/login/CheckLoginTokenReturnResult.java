package com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.login;

import com.naonao.grab12306ticket.version.springboot.resultclass.ITicketReturnResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-04-30 23:55
 **/

@Getter
@Setter
public class CheckLoginTokenReturnResult implements ITicketReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;

    private String loginUserName;
    private String apptk;

}
