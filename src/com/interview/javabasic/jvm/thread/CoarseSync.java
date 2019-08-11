package com.interview.javabasic.jvm.thread;

public class CoarseSync {
    public static String copyString100Times(String targer) {
        int i = 0;
        StringBuffer sb = new StringBuffer();
        while (i < 100) {
            sb.append(targer);
        }
        return sb.toString();
    }
}
