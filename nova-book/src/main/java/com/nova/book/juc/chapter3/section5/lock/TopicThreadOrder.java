package com.nova.book.juc.chapter3.section5.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 面试题-线程顺序打印
 * @author: wzh
 * @date: 2023/3/29 09:28
 */
@Slf4j(topic = "TopicThreadOrder")
class TopicThreadOrder {

    static final Object lock = new Object();

    static final ReentrantLock LOCK = new ReentrantLock();

    static boolean work = false;

    public static void main(String[] args) throws InterruptedException {
        //one();

        //two();

        //three();
    }

    public static void one() {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (!work) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.debug("2");
                work = true;
                lock.notify();
            }
        }, "t2");

        t1.start();
        t2.start();
    }

    public static void two() {
        Thread t1 = new Thread(() -> {
            while (!work) {
                try {
                    LOCK.lock();
                } finally {
                    LOCK.unlock();
                }
            }
            log.debug("1");
        }, "t1");

        Thread t2 = new Thread(() -> {
            LOCK.lock();
            try {
                log.debug("2");
                work = true;
            } finally {
                LOCK.unlock();
            }
        }, "t2");

        t1.start();
        t2.start();
    }

    public static void three() {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.debug("1");
        }, "t1");

        Thread t2 = new Thread(() -> {
            log.debug("2");
            LockSupport.unpark(t1);
        }, "t2");

        t1.start();
        t2.start();
    }

}
