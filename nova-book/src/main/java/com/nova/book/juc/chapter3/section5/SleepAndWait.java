package com.nova.book.juc.chapter3.section5;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

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
                    ThreadUtil.sleep(2000);

                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        ThreadUtil.sleep(1000);
        synchronized (lock) {
            log.debug("main 获得锁");
        }

        System.exit(0);
    }
}
