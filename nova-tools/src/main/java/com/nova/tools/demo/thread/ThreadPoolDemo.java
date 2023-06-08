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

        executor.execute(new DemoA());
        // 优雅关闭线程池
        executor.shutdown();
        // 任务执行完毕后打印"Done"
        System.out.println("Done");
    }

    @Test
    public void demoB() throws InterruptedException {
        TimeInterval timer = DateUtil.timer();
        for (int i = 0; i < 1000; i++) {
            queue.put(i);
        }
        for (int j = 0; j < 50; j++) {
            service.submit(new DemoB());
        }
        System.out.println("耗时:" + timer.interval());

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
            pool.execute(() -> System.out.println("I am " + number));
        }
        pool.shutdown();
    }

    /**
     * 创建ForkJoinPool线程池
     */
    @Test
    public void demoD() {
        TimeInterval timer = DateUtil.timer();
        ForkJoinPool pool = new ForkJoinPool();
        Task task = new Task(0L, 10000000000L);
        Long invoke = pool.invoke(task);
        System.err.println(invoke);
        System.out.println("花费时间:" + timer.interval() + "ms");
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

    public static class Task extends RecursiveTask<Long> {
        private final long start;
        private final long end;
        private static final long THURS_HOLD = 10000000L;

        public Task(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            long length = end - start;
            if (length <= THURS_HOLD) {//小于临界值
                long sum = 0L;
                for (long i = start; i <= end; i++) {
                    sum += i;
                }
                return sum;

            } else {
                long mid = (start + end) / 2;
                Task task1 = new Task(start, mid);
                Task task2 = new Task(mid + 1, end);
                invokeAll(task1, task2);
                return task1.join() + task2.join();
            }
        }
    }
}
