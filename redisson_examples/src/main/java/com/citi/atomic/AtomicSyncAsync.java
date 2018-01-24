package com.citi.atomic;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class AtomicSyncAsync {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);


        RAtomicLong longObject = client.getAtomicLong("myLong");
// sync way
        longObject.set(10);
        System.out.println("Value : " + longObject.getAndDecrement());
        System.out.println("Value : " + longObject.get());
// async way
        RFuture<Boolean> future = longObject.compareAndSetAsync(3, 501);
        future.thenAccept(res -> {
            System.out.println("Value OK");
        });

//        future.handle((result, exception) -> {
//            // handle the result or exception here.
//            int x = 5;
//        });
    }
}
