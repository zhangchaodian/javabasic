package com.interview.javabasic.jvm.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) throws InterruptedException {
        new CyclicBarrierDemo().go();
    }

    private void go() throws InterruptedException {
        // 初始化栅栏的参与者数为3
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        // 依次创建3个线程，并启动
        new Thread(new Task(cyclicBarrier), "Thread1").start();
        Thread.sleep(1000);
        new Thread(new Task(cyclicBarrier), "Thread2").start();
        Thread.sleep(1000);
        new Thread(new Task(cyclicBarrier), "Thread3").start();
        Thread.sleep(1000);
        System.out.println("所有线程已到达栅栏，主线程也可以开始继续往下执行" + System.currentTimeMillis());
    }


    class Task implements Runnable {
        private CyclicBarrier cyclicBarrier;

        public Task(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "已经到达栅栏" + System.currentTimeMillis());
            try {
                cyclicBarrier.await();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("所有线程都到达栅栏，线程" + Thread.currentThread().getName() + "接着往下执行" + System.currentTimeMillis());
        }
    }
}
