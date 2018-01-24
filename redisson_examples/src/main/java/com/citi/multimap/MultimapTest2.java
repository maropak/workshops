package com.citi.multimap;

import org.redisson.Redisson;
import org.redisson.api.RSetMultimap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Set;

public class MultimapTest2 {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);
        RSetMultimap<String, String> setMultimap = client.getSetMultimap("myFish");
// Adding items

// Getting size
// total entries amount
        System.out.println(setMultimap.size()); // 5
// total values amount by key
        System.out.println(setMultimap.get("favoriteFish").size()); // 3
// check entry existence
        System.out.println(setMultimap.containsEntry("favoriteFish", "Ladyfish"));
// and so on ...
    }
}
