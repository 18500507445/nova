package com.nova.tools.java8.concurrent;

import com.nova.common.utils.thread.Threads;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

/**
 * @description: 锁demo
 * @author: wzh
 * @date: 2022/11/18 15:11
 */
public class LockExample {

    private static final int NUM_INCREMENTS = 10000;

    private static final ReentrantLock lock = new ReentrantLock();

    private static int count = 0;

    private static void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void demoA() {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS).forEach(i -> executor.submit(LockExample::increment));

        Threads.stop(executor);

        System.out.println(count);
    }

    @Test
    public void demoB() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            lock.lock();
            try {
                Threads.sleep(1000);
            } finally {
                lock.unlock();
            }
        });

        executor.submit(() -> {
            System.out.println("Locked: " + lock.isLocked());
            System.out.println("Held by me: " + lock.isHeldByCurrentThread());
            boolean locked = lock.tryLock();
            System.out.println("Lock acquired: " + locked);
        });

        Threads.stop(executor);
    }

    @Test
    public void demoC() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Map<String, String> map = new HashMap<>();

        ReadWriteLock lock = new ReentrantReadWriteLock();

        executor.submit(() -> {
            lock.writeLock().lock();
            try {
                Threads.sleep(1000);
                map.put("foo", "bar");
            } finally {
                lock.writeLock().unlock();
            }
        });

        Runnable readTask = () -> {
            lock.readLock().lock();
            try {
                System.out.println(map.get("foo"));
                Threads.sleep(1000);
            } finally {
                lock.readLock().unlock();
            }
        };
        executor.submit(readTask);
        executor.submit(readTask);

        Threads.stop(executor);
    }


    @Test
    public void demoD() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Map<String, String> map = new HashMap<>();

        StampedLock lock = new StampedLock();

        executor.submit(() -> {
            long stamp = lock.writeLock();
            try {
                Threads.sleep(1000);
                map.put("foo", "bar");
            } finally {
                lock.unlockWrite(stamp);
            }
        });

        Runnable readTask = () -> {
            long stamp = lock.readLock();
            try {
                System.out.println(map.get("foo"));
                Threads.sleep(1000);
            } finally {
                lock.unlockRead(stamp);
            }
        };
        executor.submit(readTask);
        executor.submit(readTask);

        Threads.stop(executor);
    }

    @Test
    public void demoE() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        StampedLock lock = new StampedLock();

        executor.submit(() -> {
            long stamp = lock.tryOptimisticRead();
            try {
                System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                Threads.sleep(1000);
                System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                Threads.sleep(2000);
                System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
            } finally {
                lock.unlock(stamp);
            }
        });

        executor.submit(() -> {
            long stamp = lock.writeLock();
            try {
                System.out.println("Write Lock acquired");
                Threads.sleep(2000);
            } finally {
                lock.unlock(stamp);
                System.out.println("Write done");
            }
        });

        Threads.stop(executor);
    }

    @Test
    public void demoF() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        StampedLock lock = new StampedLock();

        executor.submit(() -> {
            long stamp = lock.readLock();
            try {
                if (count == 0) {
                    stamp = lock.tryConvertToWriteLock(stamp);
                    if (stamp == 0L) {
                        System.out.println("Could not convert to write lock");
                        stamp = lock.writeLock();
                    }
                    count = 23;
                }
                System.out.println(count);
            } finally {
                lock.unlock(stamp);
            }
        });

        Threads.stop(executor);
    }
}
