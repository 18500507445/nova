package com.nova.book.jvm.chapter1.section2;

/**
 * @description: 线程安全问题
 * @author: wzh
 * @date: 2023/3/17 10:23
 */
class ThreadProblem {

    public static void main(String[] args) {

    }

    /**
     * 多个线程执行此方法，局部变量是安全的
     */
    public static void m1() {
        int x = 0;
        for (int i = 0; i < 5000; i++) {
            x++;
        }
        System.err.println("x = " + x);
    }

    /**
     * int x 逃离了方法内作用范围，线程不安全的
     *
     * @param x
     */
    public static void m2(int x) {
        for (int i = 0; i < 5000; i++) {
            x++;
        }
        System.err.println("x = " + x);
    }

}
