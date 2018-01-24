package com.citi.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class Lock2 {

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);
        RLock lock = client.getLock("anyLock");

// Most familiar locking method
        // Wait for 100 seconds and automatically unlock it after 10 seconds
//        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
//        System.out.println("Is locked : " + res);

        System.out.println("Is locked : " + lock.tryLock());
        Thread.sleep(30000);
        System.out.println("Is locked : " + lock.tryLock());
        lock.unlock();
    }

}
