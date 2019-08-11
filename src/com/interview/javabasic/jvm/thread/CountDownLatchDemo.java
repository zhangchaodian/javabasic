package com.interview.javabasic.jvm.thread;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        new CountDownLatchDemo().go();
    }

    private void go() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        new Thread(new Task(countDownLatch), "Thread1").start();
        Thread.sleep(1000);
        new Thread(new Task(countDownLatch), "Thread2").start();
        Thread.sleep(1000);
        new Thread(new Task(countDownLatch), "Thread3").start();
        Thread.sleep(1000);
        // CountDownLatch的await的作用就是一直阻塞当前主线程执行，一直到计数器(有别的线程调用了countDown会导致该计数器-1)，
        // 为0，则当前主线程自动被唤醒，从调用await代码处再接着往下执行
        countDownLatch.await();
        System.out.println("所有线程已到达，主线程开始执行" + System.currentTimeMillis());
    }


    class Task implements Runnable {
        private CountDownLatch countDownLatch;

        public Task(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "已经到达" + System.currentTimeMillis());
            countDownLatch.countDown();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程" + Thread.currentThread().getName() + "虽然触发了countDown，但是还是可以执行接着的自己别的任务" + System.currentTimeMillis());
        }
    }
}

