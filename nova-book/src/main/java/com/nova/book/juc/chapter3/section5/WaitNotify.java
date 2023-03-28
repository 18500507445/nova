package com.nova.book.juc.chapter3.section5;

import com.nova.common.utils.thread.Threads;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 等待、唤醒demo
 * @author: wzh
 * @date: 2023/3/26 13:35
 */
@Slf4j(topic = "WaitNotify")
class WaitNotify {

    final static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    obj.wait(); // 让线程在obj上一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t1继续执行....");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    obj.wait(); // 让线程在obj上一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t2继续执行....");
            }
        }, "t2").start();

        // 主线程两秒后执行
        Threads.sleep(500);

        log.debug("唤醒 obj 上其它线程");

        synchronized (obj) {
            //obj.notify(); // 唤醒obj上一个线程
            obj.notifyAll(); // 唤醒obj上所有等待线程
        }
    }
}
