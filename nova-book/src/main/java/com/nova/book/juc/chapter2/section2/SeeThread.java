package com.nova.book.juc.chapter2.section2;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 观察线程交替运行，t1、t2交叉切换
 * @author: wzh
 * @date: 2023/3/24 21:36
 */
@Slf4j(topic = "SeeThread")
class SeeThread {

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                log.debug("t1，running");
            }
        }, "t1").start();

        new Thread(() -> {
            while (true) {
                log.debug("t2，running");
            }
        }, "t2").start();
    }
}
