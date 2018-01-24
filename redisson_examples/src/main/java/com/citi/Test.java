package com.citi;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");

        RedissonClient client = Redisson.create(config);
        RKeys keys = client.getKeys();
        Iterable<String> keysByPattern = keys.getKeysByPattern("testii*");
//        RMap<String, Object> map = client.getMap("testii:01200");
//        System.out.println(map.toString());
//        RBucket<Map> bucket =  client.getBucket("testii:01200");
//        Map map = bucket.get();
        RBuckets buckets = client.getBuckets();

      /*  List<RBucket<Map>> foundBuckets = buckets.find("testii*");
        Map<String, Object> loadedBuckets = buckets.get("testii:01200");
        Map map = foundBuckets.get(1).get();
        int x = 5;*/

//        Map<String, Object> map = new HashMap<>();
//        map.put("myBucket1", "aaaa");
//        map.put("myBucket2", "bbb");
//        buckets.set(map);

        Map<String, Object> map = buckets.get("myBucket1");
        int x = 5;
    }

}
