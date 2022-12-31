package com.nova.tools.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @description:
 * @author: wzh
 * @date: 2022/11/18 15:15
 */
public class SynchronizedDemo {

    private static final int NUM_INCREMENTS = 10000;

    private static int count = 0;

    private static synchronized void incrementSync() {
        count = count + 1;
    }

    private static void incrementSyncClass() {
        synchronized (SynchronizedDemo.class) {
            count = count + 1;
        }
    }

    private static void increment() {
        count = count + 1;
    }

    /**
     * testSyncIncrement
     */
    @Test
    public void demoA() {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> executor.submit(SynchronizedDemo::incrementSync));

        ConcurrentUtils.stop(executor);

        System.out.println("   Sync: " + count);
    }

    /**
     * testNonSyncIncrement
     */
    @Test
    public void demoB() {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> executor.submit(SynchronizedDemo::increment));

        ConcurrentUtils.stop(executor);

        System.out.println("NonSync: " + count);
    }

    /**
     * testSyncIncrement
     */
    @Test
    public void demoC() {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> executor.submit(SynchronizedDemo::incrementSyncClass));

        ConcurrentUtils.stop(executor);

        System.out.println(count);
    }


}