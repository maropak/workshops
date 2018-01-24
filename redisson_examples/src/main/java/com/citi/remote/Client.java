package com.citi.remote;

import org.redisson.Redisson;
import org.redisson.api.RRemoteService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Client {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);
        RRemoteService remoteService = client.getRemoteService();
        Service service = remoteService.get(Service.class);
        System.out.println(service.execute());
    }
}
