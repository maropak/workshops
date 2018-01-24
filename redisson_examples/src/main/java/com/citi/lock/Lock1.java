package com.citi.lock;

import com.citi.executor.RunnableTask;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

// distributed reentrant Lock
public class Lock1 {

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);
        RLock lock = client.getLock("anyLock");

// Most familiar locking method
        lock.lock();
        Thread.sleep(30000);
        lock.unlock();
//        System.out.println()
    }
}
