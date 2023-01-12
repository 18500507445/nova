package com.nova.tools.demo.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: 1.使用newSingleThreadExecutor  2使用join方法 3ThreadPoolExecutor，设置它的核心线程数为1
 * @author: wzh
 * @date: 2021/4/25 11:29
 */

public class OrderThread {

    public static final int TOTAL = 1000;

    public static void main(String[] args) {
        for (int j = 1; j <= TOTAL; ++j) {
            Thread thread = new Thread(new Worker(j));
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Worker implements Runnable {

        private final int number;

        public Worker(int i) {
            number = i;
        }

        @Override
        public synchronized void run() {
            System.out.println("I am " + number);
        }
    }

    /**
     * 创建single线程池
     */
    @Test
    void Pool() {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 1; i <= TOTAL; ++i) {
            final int number = i;
            pool.execute(() -> System.out.println("I am " + number));
        }
        pool.shutdown();
    }


}
