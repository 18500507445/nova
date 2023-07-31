package com.nova.book.algorithm.queqe;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 线程安全
 * 1.synchronized 代码块，属于关键字级别提供锁保护，功能少
 * 2.ReentrantLock 类，功能丰富
 * @author: wzh
 * @date: 2023/3/16 14:15
 */
class ThreadSafe {

    private final String[] array = new String[10];
    private int tail = 0;

    ReentrantLock lock = new ReentrantLock();

    /**
     * 可以在
     *
     * @param e
     */
    public void offer(String e) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            array[tail] = e;
            tail++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    /**
     * t2会进入等待状态
     *
     * @param args
     */
    public static void main(String[] args) {
        ThreadSafe queue = new ThreadSafe();
        new Thread(() -> {
            try {
                queue.offer("e1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1").start();
        new Thread(() -> {
            try {
                queue.offer("e2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2").start();
        System.err.println(queue);
    }
}
