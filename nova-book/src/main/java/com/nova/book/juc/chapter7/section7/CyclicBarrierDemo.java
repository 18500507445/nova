package com.nova.book.juc.chapter7.section7;

import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @description: 循环栅栏
 * @author: wzh
 * @date: 2023/3/30 23:17
 */
@Slf4j(topic = "CyclicBarrier")
class CyclicBarrierDemo {

    /**
     * 简单版实现 3次循环打印
     */
    @SneakyThrows
    @Test
    public void simple() {
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = new CountDownLatch(2);
            service.submit(() -> {
                log.debug("task1 start...");
                ThreadUtil.sleep(1000);
                latch.countDown();
            });
            service.submit(() -> {
                log.debug("task2 start...");
                ThreadUtil.sleep(2000);
                latch.countDown();
            });

            latch.await();
            log.debug("task1 task2 finish...");
        }
        service.shutdown();
    }

    /**
     * CyclicBarrier实现
     * 怎么读：塞克立刻拜瑞耳
     */
    public static void main(String[] args) {
        //线程数和任务数要相等，否则：task1  task2  task1
        int taskNum = 2, coreSize = 2;
        ExecutorService service = Executors.newFixedThreadPool(coreSize);
        CyclicBarrier barrier = new CyclicBarrier(taskNum, () -> {
            log.debug("task1, task2 finish...");
        });
        for (int i = 0; i < 3; i++) {
            service.submit(() -> {
                log.debug("task1 start...");
                ThreadUtil.sleep(1000);
                try {
                    barrier.await(); // 2-1=1
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            service.submit(() -> {
                log.debug("task2 start...");
                ThreadUtil.sleep(2000);
                try {
                    barrier.await(); // 1-1=0
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();
    }

}
