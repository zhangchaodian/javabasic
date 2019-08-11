package com.interview.javabasic.throwable;

public class ExceptionHandleMechanism {
    public static int doWork() {
        try {
            int i = 10 / 0; // 会抛出异常
            System.out.println("i=" + i);
        } catch (ArithmeticException e) { // 捕获异常
            System.out.println("ArithmeticException: " + e);
            return 0;
        } catch (Exception e) {
            // 捕获Exception
            System.out.println("Exception: " + e);
            return 1;
        } finally {
            System.out.println("Finally");
            return 2;
        }
    }

    public static void main(String[] args) {
        System.out.println("执行后的值为:" + doWork());
        System.out.println("Mission Complete");
    }
}
