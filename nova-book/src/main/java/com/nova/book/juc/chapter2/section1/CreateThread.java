package com.nova.book.juc.chapter2.section1;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @description: 创建线程
 * @author: wzh
 * @date: 2023/3/24 21:14
 */
@Slf4j(topic = "CreateThread")
class CreateThread {

    /**
     * 方式一
     */
    @Test
    public void demoA() {
        //创建线程对象
        Thread t = new Thread("t1") {
            @Override
            public void run() {
                //执行的任务
                log.debug("Thread创建方式");
            }
        };
        //启动
        t.start();
        printLog();
    }

    private static void printLog() {
        log.debug("main线程，running");
    }

    /**
     * 方式二，Runnable创建任务对象
     */
    @Test
    public void demoB() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.debug("Runnable创建方式");
            }
        };
        Thread t = new Thread(runnable, "t2");
        t.start();
        printLog();
    }

    /**
     * 方式三，FutureTask创建任务对象
     */
    @Test
    public void demoC() throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(() -> {
            log.debug("FutureTask创建方式");
            ThreadUtil.sleep(2000);
            return 100;
        });
        Thread t = new Thread(task, "t3");
        t.start();
        log.debug("task返回结果：{}", task.get());
    }


}
