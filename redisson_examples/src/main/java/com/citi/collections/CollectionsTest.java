package com.citi.collections;

import com.citi.Model;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class CollectionsTest {


    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);

        RMap<String, Model> map = client.getMap("anyMap");
        Model newModelr = map.put("123", new Model("aa", "bb"));

        RSet<Model> set = client.getSet("anySet");
        set.add(new Model("aaa", "bbb"));
    }

}
