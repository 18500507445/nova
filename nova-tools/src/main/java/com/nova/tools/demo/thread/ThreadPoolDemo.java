package com.nova.tools.demo.thread;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/3/31 10:38
 */
public class ThreadPoolDemo {

    /**
     * 线程数
     */
    public static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static final ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private static final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

    @Test
    public void demoA() {
        // 使用 ThreadFactoryBuilder 创建自定义线程名称的 ThreadFactory
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().build();

        // 创建线程池，其中任务队列需要结合实际情况设置合理的容量
        ThreadPoolExecutor executor = new ThreadPoolExecutor(THREAD_POOL_SIZE,
                THREAD_POOL_SIZE,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());

        executor.execute(new DemoA());
        // 优雅关闭线程池
        executor.shutdown();
        // 任务执行完毕后打印"Done"
        System.out.println("Done");
    }

    @Test
    public void demoB() throws InterruptedException {
        TimeInterval timer = DateUtil.timer();
        for (int i = 0; i < 100000; i++) {
            queue.put(i);
        }
        for (int j = 0; j < 50; j++) {
            service.submit(new DemoB());
        }
        System.out.println("耗时:" + timer.interval());

        // 优雅关闭线程池
        service.shutdown();
        service.awaitTermination(1000L, TimeUnit.SECONDS);
    }

    private static class DemoA implements Runnable {
        @Override
        public void run() {
            System.out.println("--" + Thread.currentThread().getId());
        }
    }

    private static class DemoB implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println(queue.take() + "--" + Thread.currentThread().getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
