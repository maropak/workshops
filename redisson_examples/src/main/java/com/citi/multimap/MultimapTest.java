package com.citi.multimap;

import org.redisson.Redisson;
import org.redisson.api.RSetMultimap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Set;

public class MultimapTest {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);
        RSetMultimap<String, String> setMultimap = client.getSetMultimap("myFish");
// Adding items
        setMultimap.put("favoriteFish", "Flagfin");
        setMultimap.put("favoriteFish", "Shiner");
        setMultimap.put("favoriteFish", "Ladyfish");
        setMultimap.put("oceanFish", "Shark");
        setMultimap.put("oceanFish", "Ocean sunfish");
// Removing item
        setMultimap.remove("oceanFish", "Shark");
        setMultimap.remove("favoriteFish", "Flagfin");

    }
}
