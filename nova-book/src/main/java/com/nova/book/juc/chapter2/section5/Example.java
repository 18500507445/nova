package com.nova.book.juc.chapter2.section5;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @description: 应用案例
 * @author: wzh
 * @date: 2023/3/25 08:41
 */
@Slf4j
class Example {

    static int r = 0;

    static int r1 = 0;

    /**
     * 限制对CPU的使用
     * 在没有利用cpu来计算时，不要让while(true) 空转浪费cpu，这时可以使用yield或sleep 来让出cpu的使用权给其他程序
     *
     * @throws InterruptedException
     */
    @Test
    public void sleep() throws InterruptedException {
        while (true) {
            ThreadUtil.sleep(50);
        }
    }

    /**
     * 多个线程join
     *
     * @throws InterruptedException
     */
    @Test
    public void manyThreadJoin() throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @SneakyThrows
            @Override
            public void run() {
                ThreadUtil.sleep(1);
                r = 10;
            }
        };

        Thread t2 = new Thread("t2") {
            @SneakyThrows
            @Override
            public void run() {
                ThreadUtil.sleep(2);
                r1 = 20;
            }
        };

        t1.start();
        t2.start();

        TimeInterval timer = DateUtil.timer();

        t2.join();
        t1.join();
        log.debug("r：{}，r1：{}，cost：{}", r, r1, timer.interval());
    }

    /**
     * 两阶段终止模式
     */
    @Test
    public void twoPhaseTermination() {
        TwoPhaseTermination monitor = new TwoPhaseTermination();
        monitor.start();
        ThreadUtil.sleep(3000);
        monitor.stop();
    }

    /**
     * 统筹规划，喝茶
     * 洗水壶1s、烧开水5s
     * 洗茶壶、洗茶杯、拿茶叶 4s
     * 最后泡茶
     */
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("洗水壶");
                ThreadUtil.sleep(1);
                log.debug("烧开水");
                ThreadUtil.sleep(5);
            }
        };

        Thread t2 = new Thread("t2") {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("洗茶壶");
                ThreadUtil.sleep(1);
                log.debug("洗茶杯");
                ThreadUtil.sleep(1);
                log.debug("拿茶叶");
                ThreadUtil.sleep(1);
                t1.join();
                log.debug("泡茶");
            }
        };

        t1.start();
        t2.start();
    }

}

@Slf4j
class TwoPhaseTermination {
    private Thread monitor;

    /**
     * 升级版，标识
     */
    private volatile boolean stop = false;

    /**
     * 开启
     */
    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    log.debug("料理后事的isInterrupted");
                    break;
                }
                try {
                    log.debug("执行监控记录");
                    ThreadUtil.sleep(1);
                } catch (Exception e) {
                    log.error("异常：", e);
                    //重新设置打断标记，因为catch后变为false
                    log.debug("重置前的isInterrupted：{}", current.isInterrupted());
                    current.interrupt();
                    log.debug("重置后的isInterrupted：{}", current.isInterrupted());
                }
            }
        });
        monitor.start();
    }

    /**
     * 停止线程
     */
    public void stop() {
        monitor.interrupt();
    }

    public void startPro() {
        monitor = new Thread(() -> {
            while (true) {
                if (stop) {
                    log.debug("料理后事stop");
                    break;
                }
                log.debug("执行监控记录");
                ThreadUtil.sleep(1);
            }
        });
        monitor.start();
    }

    public void stopPro() {
        stop = true;
        monitor.interrupt();
    }

}
