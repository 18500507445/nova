package com.nova.book.juc.chapter3.section5.lock;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 活锁
 * @author: wzh
 * @date: 2023/3/28 15:54
 */
@Slf4j(topic = "LiveLock")
class LiveLock {

    static volatile int count = 10;

    public static void main(String[] args) {
        new Thread(() -> {
            // 期望减到 0 退出循环
            while (count > 0) {
                ThreadUtil.sleep(200);
                count--;
                log.debug("count: {}", count);
            }
        }, "t1").start();

        new Thread(() -> {
            // 期望超过 20 退出循环
            while (count < 20) {
                ThreadUtil.sleep(200);
                count++;
                log.debug("count: {}", count);
            }
        }, "t2").start();
    }
}
