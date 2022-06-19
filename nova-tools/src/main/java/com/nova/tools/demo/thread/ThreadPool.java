package com.nova.tools.demo.thread;

import java.util.concurrent.*;


/**
 * @author wangzehui
 */
public class ThreadPool {

    private static int nThreads = Runtime.getRuntime().availableProcessors();

    private static ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);

    private static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            queue.put(i);
        }
        for (int j = 0; j < 50; j++) {
            service.submit(new Test());
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start));

        // 优雅关闭线程池
        service.shutdown();
        service.awaitTermination(1000L, TimeUnit.SECONDS);
    }

    private static class Test implements Runnable {
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
