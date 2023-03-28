package com.nova.book.juc.chapter3.section5;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description: sleep和wait的区别
 * @author: wzh
 * @date: 2023/3/26 13:53
 */
@Slf4j(topic = "SleepAndWait")
class SleepAndWait {

    static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock) {
                log.debug("t1 获得锁");

                try {
                    //TimeUnit.SECONDS.sleep(2000);

                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);
        synchronized (lock) {
            log.debug("main 获得锁");
        }

        System.exit(0);
    }
}
