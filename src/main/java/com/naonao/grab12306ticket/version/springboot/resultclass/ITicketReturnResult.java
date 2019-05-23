package com.naonao.grab12306ticket.version.springboot.resultclass;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-17 18:25
 **/
public interface ITicketReturnResult {

    Boolean getStatus();

    void setStatus(Boolean status);

    String getMessage() ;

    void setMessage(String message);

    CloseableHttpClient getSession();

    void setSession(CloseableHttpClient session);

}
