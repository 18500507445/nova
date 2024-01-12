package com.nova.book.juc.chapter2.section5;

import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


/**
 * @description: 线程方法
 * @author: wzh
 * @date: 2023/3/25 08:03
 */
@Slf4j(topic = "ThreadMethod")
class ThreadMethod {

    static int r = 0;

    /**
     * 直接运行，没开启线程
     */
    @Test
    public void run() {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running");
            }
        };
        t1.run();
    }

    @Test
    public void sleep() throws InterruptedException {
        Thread t2 = new Thread("t2") {
            @SneakyThrows
            @Override
            public void run() {
                ThreadUtil.sleep(2000);
            }
        };

        log.debug("t2 state:{}", t2.getState());
        t2.start();
        log.debug("t2 state:{}", t2.getState());
        ThreadUtil.sleep(500);
        log.debug("t2 state:{}", t2.getState());
    }

    @Test
    public void interrupt() throws InterruptedException {
        Thread t3 = new Thread("t3") {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("enter sleep");
                ThreadUtil.sleep(2000);
            }
        };
        t3.start();

        ThreadUtil.sleep(1000);
        log.debug("interrupt....");
        t3.interrupt();
    }

    /**
     * 优雅停止，自己判断，料理后事
     */
    @Test
    public void interruptFor() {
        Thread t3 = new Thread(() -> {
            while (true) {
                boolean interrupted = Thread.currentThread().isInterrupted();
                if (interrupted) {
                    log.debug("被打断了，退出循环");
                    break;
                }
            }
        }, "t3");
        t3.start();
        ThreadUtil.sleep(1000);
        log.debug("interrupt....");
        t3.interrupt();
    }


    /**
     * 让出资源
     *
     * @throws InterruptedException
     */
    @Test
    public void yield() throws InterruptedException {
        Thread t4 = new Thread("t4") {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("enter yield");
                Thread.yield();
            }
        };
        log.debug("t4 state:{}", t4.getState());
        t4.start();
        log.debug("t4 state:{}", t4.getState());
        ThreadUtil.sleep(500);
        log.debug("t4 state:{}", t4.getState());
    }

    /**
     * 设置优先级，结果应该是t2线程增长高一些，但是具体还是依赖于操作系统的任务调度器
     */
    @Test
    public void priority() {
        Runnable task1 = () -> {
            int count = 0;
            while (true) {
                System.err.println("----task1 " + count++);
            }
        };

        Runnable task2 = () -> {
            int count = 0;
            while (true) {
                System.err.println("            ----task2 " + count++);
            }
        };

        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");

        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);

        t1.start();
        t2.start();
    }

    /**
     * 分析：因为主线程和线程t5是并行执行的，t5线程需要1秒之后才能算出r=10，而主线程一开始就要打印r的结果，所以只能打印出r=0
     * 结论：用join，t5调用就等待t5结束后，返回结果
     */
    @Test
    public void join() throws InterruptedException {
        log.debug("main，开始");
        Thread t5 = new Thread("t5") {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("t5，开始");
                ThreadUtil.sleep(1000);
                log.debug("t5，结束");
                r = 10;
            }
        };
        t5.start();
        //t5.join();
        log.debug("main，结果为：{}", r);
    }

    @Test
    public void joinTime() throws InterruptedException {
        Thread t6 = new Thread("t6") {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("t6，开始");
                ThreadUtil.sleep(2000);
                log.debug("t6，结束");
                r = 10;
            }
        };
        t6.start();
        t6.join(1500);
        log.debug("main，结果为：{}", r);
    }

    /**
     * 守护线程
     */
    @Test
    public void daemon() throws InterruptedException {
        Thread t7 = new Thread("t7") {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    boolean interrupted = Thread.currentThread().isInterrupted();
                    if (interrupted) {
                        log.debug("被打断了，退出循环");
                        break;
                    }
                }
            }
        };
        //设置该线程为守护线程
        t7.setDaemon(true);
        t7.start();
        ThreadUtil.sleep(1000);
        log.debug("main，结果为：{}", r);
    }


}
