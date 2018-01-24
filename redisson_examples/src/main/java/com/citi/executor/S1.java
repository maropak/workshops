package com.citi.executor;

import org.redisson.Redisson;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class S1 {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);
        RScheduledExecutorService executorService = client.getExecutorService("myExecutor");
        executorService.scheduleAtFixedRate(new RunnableTask(), 10, 15, TimeUnit.SECONDS);
    }

}
