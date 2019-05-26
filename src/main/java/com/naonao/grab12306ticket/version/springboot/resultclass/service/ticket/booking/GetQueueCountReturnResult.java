package com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.booking;

import com.naonao.grab12306ticket.version.springboot.resultclass.ITicketReturnResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-05 20:08
 **/
@Getter
@Setter
public class GetQueueCountReturnResult implements ITicketReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;

}
