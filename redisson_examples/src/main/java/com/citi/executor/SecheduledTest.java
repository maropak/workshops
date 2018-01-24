package com.citi.executor;

public class SecheduledTest {

    public static void main(String[] args) throws InterruptedException {
        new Thread(new ExecutorProc(true)).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new ExecutorProc(false)).start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
