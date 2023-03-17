package com.nova.tools.demo.thread;

/**
 * @description:
 * 1.使用newSingleThreadExecutor
 * 2.使用join方法 3ThreadPoolExecutor，设置它的核心线程数为1
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


}
