package com.citi.liveobject;

import org.redisson.Redisson;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Instance2 {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);


        RLiveObjectService service = client.getLiveObjectService();
        service.registerClass(MyOtherObject.class);
        MyOtherObject myOtherObject = service.get(MyOtherObject.class, "MyOtherObject1");
        System.out.println(myOtherObject.getValue());

        System.out.println(myOtherObject.getValue());

        MyOtherObject myOtherObject2 = service.get(MyOtherObject.class, "MyOtherObject3");
        System.out.println("MyOtherObject2 : " + myOtherObject2.getValue());

    }
}
