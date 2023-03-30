package com.nova.book.juc.chapter7.section5;

import com.nova.common.utils.thread.Threads;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * @description:
 * @author: wzh
 * @date: 2023/3/30 22:13
 */
@Slf4j(topic = "Semaphore")
class SemaphoreDemo {

    public static void main(String[] args) {
        // 1. 创建 semaphore 对象
        Semaphore semaphore = new Semaphore(3);

        /**
         * 2. 6个线程同时运行
         */
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    log.debug("running...");
                    Threads.sleep(1000);
                    log.debug("end...");
                } finally {
                    semaphore.release();
                }
            }).start();
        }
    }
}
