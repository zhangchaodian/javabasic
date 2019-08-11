package com.interview.javabasic.jvm.thread;

public class StringBufferWithoutSync {
    public void add(String str1, String str2) {
        StringBuffer sb = new StringBuffer();
        sb.append(str1).append(str2);
    }

    public static void main(String[] args) {
        StringBufferWithoutSync withoutSync = new StringBufferWithoutSync();
        for (int i = 0; i < 1000; i++) {
            withoutSync.add("aaa", "bbb");
        }
    }
}
