package com.nova.tools.demo.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 从运行结果可以看出，当一个线程运行完毕后才把锁释放，其他线程才能执行，其他线程的执行顺序是不确定的
 * @author: wzh
 * @date: 2021/4/25 11:19
 */

public class JavaLock {

    public static void main(String[] args) {
        MyService service = new MyService();

        MyThread a1 = new MyThread(service);
        MyThread a2 = new MyThread(service);
        MyThread a3 = new MyThread(service);

        a1.start();
        a2.start();
        a3.start();
    }

    static public class MyService {

        private final Lock lock = new ReentrantLock();

        public void testMethod() {
            lock.lock();
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println("ThreadName=" + Thread.currentThread().getName() + (" " + (i + 1)));
                }
            } finally {
                lock.unlock();
            }
        }

    }

    static public class MyThread extends Thread {

        private final MyService service;

        public MyThread(MyService service) {
            super();
            this.service = service;
        }

        @Override
        public void run() {
            service.testMethod();
        }
    }
}
