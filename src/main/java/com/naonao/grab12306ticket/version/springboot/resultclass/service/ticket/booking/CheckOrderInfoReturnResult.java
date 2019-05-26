package com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.booking;

import com.naonao.grab12306ticket.version.springboot.resultclass.ITicketReturnResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-01 23:38
 **/
@Getter
@Setter
public class CheckOrderInfoReturnResult implements ITicketReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;

    private String passengerTicketStr;
    private String oldPassengerStr;
    private String seatType;

}
