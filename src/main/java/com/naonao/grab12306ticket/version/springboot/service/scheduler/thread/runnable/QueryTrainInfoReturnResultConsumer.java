package com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.runnable;


import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.method.QueryTrainInfoReturnResultHandle;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-07 22:07
 **/
@Slf4j
public class QueryTrainInfoReturnResultConsumer implements Runnable{


    private QueryTrainInfoReturnResult queryTrainInfoReturnResult;

    /**
     * database version
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     */
    public QueryTrainInfoReturnResultConsumer(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        this.queryTrainInfoReturnResult = queryTrainInfoReturnResult;
    }


    /**
     * processing queryTrainInfoReturnResult
     */
    @Override
    public void run() {
        // here write queryTrainInfoReturnResult processing method after getting train information
        if (queryTrainInfoReturnResult != null){
            log.info("get a information by queue, hash ---> " + queryTrainInfoReturnResult.getStatusInformationEntity().getHash());
            try{
                new QueryTrainInfoReturnResultHandle(queryTrainInfoReturnResult).handle();
            }catch (Exception e){
                log.error(e.getMessage());
            }

        }
    }

}
