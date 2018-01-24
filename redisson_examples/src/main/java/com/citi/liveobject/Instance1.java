package com.citi.liveobject;

import org.redisson.Redisson;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Instance1 {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);

        RLiveObjectService service = client.getLiveObjectService();
        service.registerClass(MyOtherObject.class);

        MyOtherObject myOtherObject5 = service.get(MyOtherObject.class, "MyOtherObject5");
        myOtherObject5.setValue("abc");
        System.out.println("MyOtherObject3 : " + myOtherObject5.getValue());

        MyOtherObject myOtherObject = service.get(MyOtherObject.class, "MyOtherObject1");
        myOtherObject.setValue("value1");
//        service.merge(myOtherObject);
int x = 5;
        myOtherObject.setValue("value2");

        // internal object is not merged

        MyOtherObject myOtherObject4 = new MyOtherObject("MyOtherObject4");
        service.merge(myOtherObject4);
        MyOtherObject myOtherObject3 = service.get(MyOtherObject.class, "MyOtherObject3");
        myOtherObject3.setValue("abc");
        System.out.println("MyOtherObject3 : " + myOtherObject3.getValue());

        // merge and attach not perisist
        MyOtherObject myOtherObject2 = new MyOtherObject("MyOtherObject2");
//
//        service.attach(myOtherObject2);
        if (service.isExists(myOtherObject4)) {
            service.merge(myOtherObject2);
            myOtherObject2.setValue("value4");
        } else {
            service.attach(myOtherObject2);
            myOtherObject2.setValue("value3");
//            service.persist(myOtherObject2);
        }
        System.out.println("MyOtherObject2 : " + myOtherObject2.getValue());

        // scenarion - create merge, set - no, get and set - exists, before was getOrCreate
        // new object persist ,
    }
}
