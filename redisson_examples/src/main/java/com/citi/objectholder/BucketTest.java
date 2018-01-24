package com.citi.objectholder;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class BucketTest {

        public static void main(String[] args) {
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
            RedissonClient client = Redisson.create(config);
            RBucket<Model> bucket = client.getBucket("model");
            bucket.set(new Model("test"));
            Model model = bucket.get();
            System.out.println(model.getName());
    }
}
