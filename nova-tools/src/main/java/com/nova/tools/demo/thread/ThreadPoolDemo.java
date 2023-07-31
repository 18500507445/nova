package com.nova.tools.demo.thread;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @description: 线程池demo
 * 为什么使用线程池:创建和销毁线程耗费大量的时间,效率很低 为了提高线程的利用率使用线程池
 * @author: wzh
 * @date: 2022/3/31 10:38
 */
class ThreadPoolDemo {

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

        executor.execute(() -> System.err.println("--" + Thread.currentThread().getId()));
        // 优雅关闭线程池
        executor.shutdown();
        // 任务执行完毕后打印"Done"
        System.err.println("Done");
    }

    @Test
    public void demoB() throws InterruptedException {
        TimeInterval timer = DateUtil.timer();
        for (int i = 0; i < 1000; i++) {
            queue.put(i);
        }
        for (int j = 0; j < 50; j++) {
            service.submit((Runnable) () -> {
                while (true) {
                    try {
                        System.err.println(queue.take() + "--" + Thread.currentThread().getId());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        System.err.println("耗时:" + timer.interval());

        // 优雅关闭线程池
        service.shutdown();
        service.awaitTermination(3, TimeUnit.SECONDS);
    }

    /**
     * 创建single线程池
     */
    @Test
    public void demoC() {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 1; i <= 1000; ++i) {
            final int number = i;
            pool.execute(() -> System.err.println("I am " + Thread.currentThread().getId() + "-" + number));
        }
        pool.shutdown();
    }

}
