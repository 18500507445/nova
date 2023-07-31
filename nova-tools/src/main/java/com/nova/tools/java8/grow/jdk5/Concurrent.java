package com.nova.tools.java8.grow.jdk5;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 并发库
 *
 * @author wzh
 * @date 2018/2/8
 */
class Concurrent {

    public void lock() {
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            System.err.println("hello world");
        } finally {
            lock.unlock();
        }
    }

    public void condition() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        // do something
        condition.await(10, TimeUnit.SECONDS);
        System.err.println("Get result.");
    }

    public void executorService() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.err.println("Task is running.");
            }
        });
    }

    public void blockingDeque() {
        Queue<Integer> blockingDeque = new ArrayBlockingQueue<>(20);
        blockingDeque.add(1);
        blockingDeque.add(2);
        blockingDeque.add(3);

        blockingDeque.peek();
    }

    public void concurrentHashMap() {
        Map<String, Integer> concurrentHashMap = new ConcurrentHashMap<>(16);
        concurrentHashMap.put("Hello", 1);
        concurrentHashMap.put("World", 2);

        System.err.println(concurrentHashMap.get("Hello"));
    }

    public void copyOnWriteList() {
        List<String> copyOnWriteList = new CopyOnWriteArrayList<>();
        copyOnWriteList.add("a");
        copyOnWriteList.add("b");
        copyOnWriteList.add("c");

        System.err.println(copyOnWriteList.size());
    }

    public void semaphore() {
        Semaphore semaphore = new Semaphore(3);
        try {
            semaphore.acquire();
            System.err.println(Thread.currentThread().getName() + " is working");
            TimeUnit.SECONDS.sleep(1);
            semaphore.release();
            System.err.println(Thread.currentThread().getName() + " is over");
        } catch (InterruptedException e) {
        }
    }

}
