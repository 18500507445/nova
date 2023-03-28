package com.nova.tools.java8.concurrent;

import com.nova.common.utils.thread.Threads;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @description: 信号量
 * @author: wzh
 * @date: 2022/11/18 15:15
 */
public class SemaphoreDemo {

    private static final int NUM_INCREMENTS = 10000;

    private static Semaphore semaphore = new Semaphore(1);

    private static int count = 0;

    private static void increment() {
        boolean permit = false;
        try {
            permit = semaphore.tryAcquire(5, TimeUnit.SECONDS);
            count++;
        } catch (InterruptedException e) {
            throw new RuntimeException("could not increment");
        } finally {
            if (permit) {
                semaphore.release();
            }
        }
    }

    private static void doWork() {
        boolean permit = false;
        try {
            permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
            if (permit) {
                System.out.println("Semaphore acquired");
                Threads.sleep(5000);
            } else {
                System.out.println("Could not acquire semaphore");
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            if (permit) {
                semaphore.release();
            }
        }
    }

    /**
     * testIncrement
     */
    @Test
    public void demoA() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> executor.submit(SemaphoreDemo::increment));

        Threads.stop(executor);

        System.out.println("Increment: " + count);
    }

    @Test
    public void demoB() {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        IntStream.range(0, 10)
                .forEach(i -> executor.submit(SemaphoreDemo::doWork));

         Threads.stop(executor);
    }

}
