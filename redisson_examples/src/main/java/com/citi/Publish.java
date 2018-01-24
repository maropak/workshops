package com.citi;

import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;

public class Publish {

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Subscriber()).start();
        new Thread(new Subscriber()).start();
        new Thread(new Publisher()).start();
    }

    static class Subscriber implements Runnable {

        private RTopic<Model> topic;

        Subscriber() {
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
            RedissonClient client = Redisson.create(config);
            topic = client.getTopic("anyTopic");
        }

        @Override
        public void run() {

            topic.addListener(new MessageListener<Model>() {
                @Override
                public void onMessage(String channel, Model message) {
                   System.out.println(Thread.currentThread().getName() + "   " + message.getValue());
                }
            });
        }
    }

    static class Publisher implements Runnable {

        private RTopic<Model> topic;

        Publisher() {
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
            RedissonClient client = Redisson.create(config);
            topic = client.getTopic("anyTopic");
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                long clientsReceivedMessage = topic.publish(new Model("aaa", "bbb" + i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
