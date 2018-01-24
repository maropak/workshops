package com.citi.executor;

import org.redisson.Redisson;
import org.redisson.api.RExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

class ExecutorProc implements Runnable {

    private boolean submit;

    private RExecutorService executorService;

    public ExecutorProc(boolean submit) {
        this.submit = submit;
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);
        executorService = client.getExecutorService("myExecutor");
    }

    @Override
    public void run() {
        System.out.println("Started");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Will be ubmitted : " + submit);
        if (submit) {
            System.out.println("Submitted");
            executorService.execute(new Task());
        }
        System.out.println("aaaa");
        System.out.println("Submittedxxxxxx" + executorService.countActiveWorkers());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
