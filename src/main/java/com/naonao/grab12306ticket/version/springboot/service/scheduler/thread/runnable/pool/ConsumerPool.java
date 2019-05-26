package com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.runnable.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.naonao.grab12306ticket.version.springboot.mapper.StatusInformationMapper;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.runnable.QueryTrainInfoReturnResultConsumer;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.strategy.RejectExecutionHandlerBlocking;
import com.naonao.grab12306ticket.version.springboot.util.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 20:22
 **/
@Slf4j
public class ConsumerPool implements Runnable{
    

    private QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue;

    private StatusInformationMapper statusInformationMapper;

    /**
     * database version
     * @param queryTrainInfoReturnResultQueue   queryTrainInfoReturnResultQueue
     */
    public ConsumerPool(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue){
        this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
        this.statusInformationMapper = ApplicationContextProvider.getBean(StatusInformationMapper.class);
    }



    @Override
    public void run() {
        consume();
    }

    /**
     * get QueryTrainInfoReturnResult from queue, then get a thread by thread pool
     * for handle QueryTrainInfoReturnResult.
     *
     */
    public void consume(){

        int corePoolSize = 32;
        int maximumPoolSize = 64;
        long keepAliveTime = 60L;
        int queueSize = 128;

        // create thread pool
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Consume QueryTrainInfoReturnResult[%d]")
                .build();
        ExecutorService pool = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize),
                threadFactory,
                new RejectExecutionHandlerBlocking()
        );
        // get QueryTrainInfoReturnResult from queue
        try{
            while (true) {
                QueryTrainInfoReturnResult queryTrainInfoReturnResult = queryTrainInfoReturnResultQueue.consumer();
                if (queryTrainInfoReturnResult != null){
                    // database version
                    pool.execute(new QueryTrainInfoReturnResultConsumer(queryTrainInfoReturnResult));
                }
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }
        pool.shutdown();
    }





}
