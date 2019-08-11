package com.interview.javabasic.jvm.thread;

public class SyncBlockAndMethod {
    public void syncsTask() {
        //同步代码块
        synchronized (this) {
            System.out.println("Hello");
            //同步代码块
            synchronized (this) {
                System.out.println("Hello");

            }
        }
    }

    public synchronized void syncTask() {
        System.out.println("Hello Again");
    }
}
