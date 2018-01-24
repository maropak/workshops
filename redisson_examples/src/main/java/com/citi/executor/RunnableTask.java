package com.citi.executor;

import org.redisson.api.annotation.RInject;

import java.util.concurrent.TimeUnit;

public class RunnableTask  implements Runnable {

//    @RInject
//    private RedissonClient redissonClient;

    private long param;

    public RunnableTask() {
    }

    public RunnableTask(long param) {
        this.param= param;
    }

    @Override
    public void run() {
//        RAtomicLong atomic = redissonClient.getAtomicLong("myAtomic");
//        atomic.addAndGet(param);
        System.out.println("Abc");
    }

}
