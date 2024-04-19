package com.nova.book.juc.chapter7.section5;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * @description: Semaphore例子
 * 使用Semaphore限流，在访问高峰期时，让请求线程阻塞，高峰期过去再释放许可，当然它只适合限制单机
 * 线程数量，并且仅是限制线程数，而不是限制资源数（例如连接数，请对比TomcatLimitLatch的实现）
 * 用Semaphore实现简单连接池，对比『享元模式』下的实现（用wait notify），性能和可读性显然更好，
 * 注意下面的实现中线程数和数据库连接数是相等的
 * @author: wzh
 * @date: 2023/3/30 22:13
 */
@Slf4j(topic = "Semaphore")
class SemaphoreDemo {

    public static void main(String[] args) {
        // 1. 创建 semaphore 对象
        Semaphore semaphore = new Semaphore(3);

        // 2. 6个线程同时运行
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    log.error("异常：", e);
                }
                try {
                    log.debug("running...");
                    ThreadUtil.sleep(1000);
                    log.debug("end...");
                } finally {
                    semaphore.release();
                }
            }).start();
        }
    }
}
