package com.naonao.grab12306ticket.version.springboot.service.scheduler.queue;


import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.query.QueryTrainInfoReturnResult;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * create a queue for storage QueryTrainInfoReturnResult
 *
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-07 21:55
 **/
@Slf4j
public class QueryTrainInfoReturnResultQueue {


    

    private ArrayBlockingQueue<QueryTrainInfoReturnResult> queryTrainInfoReturnResultsQueue;

    public QueryTrainInfoReturnResultQueue(Integer queueSize){
        this.queryTrainInfoReturnResultsQueue = new ArrayBlockingQueue<QueryTrainInfoReturnResult>(queueSize);
    }

    public void producer(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        try{
            queryTrainInfoReturnResultsQueue.put(queryTrainInfoReturnResult);
        }catch (InterruptedException e){
            log.error(e.getMessage());
        }
    }

    public QueryTrainInfoReturnResult consumer(){
        try{
            return queryTrainInfoReturnResultsQueue.take();
        }catch (InterruptedException e){
            log.error(e.getMessage());
        }
        return null;
    }





}
