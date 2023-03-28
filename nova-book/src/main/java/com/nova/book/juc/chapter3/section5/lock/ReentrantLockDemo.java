package com.nova.book.juc.chapter3.section5.lock;

import com.nova.common.utils.thread.Threads;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 可重入锁
 * @author: wzh
 * @date: 2023/3/28 21:13
 */
@Slf4j(topic = "ReentrantLockDemo")
class ReentrantLockDemo {

    static ReentrantLock Lock = new ReentrantLock();

    public static void main(String[] args) {
        //m1();

        //interrupt();

        tryLock();
    }

    /**
     * 可重入
     */
    public static void m1() {
        Lock.lock();
        try {
            log.debug("execute m1");
            m2();
        } finally {
            Lock.unlock();
        }
    }

    public static void m2() {
        Lock.lock();
        try {
            log.debug("execute m2");
        } finally {
            Lock.unlock();
        }
    }

    /**
     * 锁可打断
     */
    public static void interrupt() {
        Thread t1 = new Thread(() -> {
            try {
                log.debug("尝试获取锁");
                Lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("没有获取到锁，返回");
                return;
            }
            try {
                log.debug("获取到锁");
            } finally {
                Lock.unlock();
            }
        }, "t1");

        Lock.lock();
        t1.start();

        Threads.sleep(1000);

        log.debug("打断 t1");
        t1.interrupt();
    }

    /**
     * 锁超时
     */
    public static void tryLock() {
        Thread t2 = new Thread(() -> {
            try {
                boolean flag = Lock.tryLock(2, TimeUnit.SECONDS);
                log.debug("尝试获得锁：{}", flag);
                if (!flag) {
                    log.debug("没有获取到锁。。");
                    return;
                }
                log.debug("拿到锁了。。");
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("没有获取到锁。。");
                return;
            } finally {
                Lock.unlock();
            }
        }, "t2");

        Lock.lock();
        log.debug("拿到锁了。。");
        t2.start();
        Threads.sleep(1000);
        Lock.unlock();
        log.debug("释放了锁。。");
    }

}
