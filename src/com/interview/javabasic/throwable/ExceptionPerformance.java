package com.interview.javabasic.throwable;

public class ExceptionPerformance {
    public static void testException(String[] array) {
        try {
            System.out.println(array[0]);
        } catch (NullPointerException e) {
            System.out.println("array cannot be null");
        }
    }

    public static void testIf(String[] array) {
        if (array != null) {
            System.out.println(array[0]);
        } else {
            System.out.println("array cannot be null");
        }
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        testException(null);
        // testIf(null);
        System.out.println("cost " + (System.nanoTime() - start));
    }
}
