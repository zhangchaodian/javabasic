package com.interview.javabasic.jvm.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    public static void main(String[] args) {
        // 线程池
        ExecutorService exec = Executors.newCachedThreadPool();
        // 只能5个线程同时访问
        final Semaphore semp = new Semaphore(5);
        // 模拟20个客户端访问
        for (int index = 0; index < 20; index++) {
            final int NO = index;
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        // 获取许可
                        semp.acquire();
                        System.out.println(Thread.currentThread().getName() + "拿到许可");
                        Thread.sleep(5000);
                        System.out.println(Thread.currentThread().getName() + "执行完了，释放许可");
                        // 访问完后，释放
                        semp.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            exec.execute(run);
        }
    }
}
