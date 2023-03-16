package com.nova.book.algorithm.queqe;

import java.util.Arrays;

/**
 * @description: 测试线程不安全
 * @author: wzh
 * @date: 2023/3/16 14:05
 */
class ThreadUnsafe {

    private final String[] array = new String[10];
    private int tail = 0;

    /**
     * @param e
     */
    public void offer(String e) {
        array[tail] = e;
        tail++;
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    /**
     * debug 线程模式
     * 多线程下交替执行，e2会覆盖e1
     *
     * @param args
     */
    public static void main(String[] args) {
        ThreadUnsafe queue = new ThreadUnsafe();
        new Thread(() -> queue.offer("e1"), "t1").start();
        new Thread(() -> queue.offer("e2"), "t2").start();
        System.out.println(queue);
    }

}
