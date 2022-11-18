package com.nova.tools.demo.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 1.使用newSingleThreadExecutor  2使用join方法 3ThreadPoolExecutor，设置它的核心线程数为1
 * @Author: wangzehui
 * @Date: 2021/4/25 11:29
 */

public class OrderThread {

    public static void main(String[] args) {
        for (int j = 0; j < 1000; ++j) {
            Thread thread = new Thread(new Worker(j));
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建single线程池
     */
    void Pool() {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 100; ++i) {
            final int number = i;
            pool.execute(() -> System.out.println("I am " + number));
        }
        pool.shutdown();
    }

    public static class Worker implements Runnable {

        private int number;

        public Worker(int i) {
            number = i;
        }

        @Override
        public synchronized void run() {
            System.out.println("I am " + number);
        }
    }
}
