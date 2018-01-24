package com.citi.collections;

import com.citi.Model;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class CollectionsGetTest {


    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);

        RMap<String, Model> map = client.getMap("anyMap");
        Model newModel = map.get("123");
        System.out.println(newModel.getValue());

        RSet<Model> set = client.getSet("anySet");
        System.out.println(set.size());
    }

}
