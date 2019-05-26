package com.naonao.grab12306ticket.version.springboot.service.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.naonao.grab12306ticket.version.springboot.mapper.*;
import com.naonao.grab12306ticket.version.springboot.service.base.AbstractScheduler;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.strategy.RejectExecutionHandlerBlocking;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.runnable.pool.ProducerPool;
import com.naonao.grab12306ticket.version.springboot.service.scheduler.thread.runnable.pool.ConsumerPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.*;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 17:37
 **/
@Slf4j
@Component
public class Scheduler extends AbstractScheduler {

    @Autowired
    protected UserInformationMapper userInformationMapper;

    @Autowired
    protected GrabTicketInformationMapper grabTicketInformationMapper;

    @Autowired
    protected NotificationInformationMapper notificationInformationMapper;

    @Autowired
    protected StatusInformationMapper statusInformationMapper;

    @Autowired
    protected AllInformationMapper allInformationMapper;

    private Set<String> hashSet = ConcurrentHashMap.<String> newKeySet();

    public void start(){
        ExecutorService pool = createThreadPool();
        QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue = createQueryTrainInfoReturnResultQueue();
        pool.execute(new ConsumerPool(
                queryTrainInfoReturnResultQueue
        ));
        pool.execute(new ProducerPool(
                queryTrainInfoReturnResultQueue
        ));
    }

    /**
     * create a thread pool for start ProducerPool and ConsumePool
     * @return  ExecutorService
     */
    private ExecutorService createThreadPool(){
        int corePoolSize = 2;
        int maximumPoolSize = 2;
        long keepAliveTime = 60L;
        int queueSize = 4;
        // create thread pool
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Scheduler[%d]")
                .build();
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize),
                threadFactory,
                new RejectExecutionHandlerBlocking()
        );
    }

    /**
     * create a QueryTrainInfoReturnResultQueue for storage QueryTrainInfoReturnResult
     * @return  QueryTrainInfoReturnResultQueue
     */
    private QueryTrainInfoReturnResultQueue createQueryTrainInfoReturnResultQueue(){
        return new QueryTrainInfoReturnResultQueue(16);
    }



}
