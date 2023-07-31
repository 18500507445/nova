package com.nova.book.juc.chapter7.section6;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.nova.common.utils.thread.Threads;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 倒计时锁（类比join）
 * @author: wzh
 * @date: 2023/3/30 22:35
 */
@Slf4j(topic = "CountDownLatch")
class CountDownLatchDemo {

    public static void main(String[] args) {

        //simple();

        threadPool();

        //game();
    }

    /**
     * 简单版
     */
    private static void simple() {
        CountDownLatch latch = new CountDownLatch(3);
        final TimeInterval timer = DateUtil.timer();
        new Thread(() -> {
            log.debug("begin...");
            Threads.sleep(1000);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        }, "t1").start();

        new Thread(() -> {
            log.debug("begin...");
            Threads.sleep(2000);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        }, "t2").start();

        new Thread(() -> {
            log.debug("begin...");
            Threads.sleep(1500);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        }, "t3").start();

        log.debug("waiting...");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("wait end...  共耗时:{}ms", timer.interval());
    }

    /**
     * 线程池版
     * <p>
     * 应用场景：(1)数据库查询不同表进行组装数据   (2)http调用不同接口进行组装数据
     */
    private static void threadPool() {
        final TimeInterval timer = DateUtil.timer();
        ExecutorService service = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(3);
        service.submit(() -> {
            log.debug("begin...");
            Threads.sleep(1000);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        });
        service.submit(() -> {
            log.debug("begin...");
            Threads.sleep(2000);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        });
        service.submit(() -> {
            log.debug("begin...");
            Threads.sleep(1500);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        });
        service.submit(() -> {
            try {
                log.debug("waiting...");
                latch.await();
                log.debug("wait end...  共耗时:{}ms", timer.interval());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        service.shutdown();
    }

    /**
     * 游戏加载玩家-应用版
     */
    private static void game() {
        AtomicInteger num = new AtomicInteger(1);
        ExecutorService service = Executors.newFixedThreadPool(10, (r) -> {
            return new Thread(r, "玩家：" + num.getAndIncrement());
        });
        CountDownLatch latch = new CountDownLatch(10);
        String[] all = new String[11];
        Random r = new Random();
        for (int j = 0; j < 10; j++) {
            int x = j;
            service.submit(() -> {
                for (int i = 0; i <= 100; i++) {
                    Threads.sleep(r.nextInt(100));
                    all[x] = Thread.currentThread().getName() + "(" + (i + "%") + ")";
                    System.err.print("\r" + Arrays.toString(all));
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        all[all.length - 1] = "总加载[100%]";
        System.err.print("\r" + Arrays.toString(all));

        System.err.println("\n游戏开始...全军出击！！！");

        service.shutdown();
    }

}
