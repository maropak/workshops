package com.citi.remote;

import org.redisson.Redisson;
import org.redisson.api.RRemoteService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Server {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);
        ServiceImpl yourService = new ServiceImpl();
        RRemoteService remoteService = client.getRemoteService();
        remoteService.register(Service.class, yourService);
    }
}
