package com.nova.tools.java8.concurrent;

import com.nova.common.utils.thread.Threads;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @description: 锁
 * @author: wzh
 * @date: 2022/11/18 15:15
 */
class SynchronizedDemo {

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

    private static final ExecutorService executor = Executors.newFixedThreadPool(2);


    /**
     * testSyncIncrement
     */
    @Test
    public void demoA() {
        count = 0;

        IntStream.range(0, NUM_INCREMENTS).forEach(i -> executor.submit(SynchronizedDemo::incrementSync));

        Threads.stop(executor);

        System.err.println("Sync: " + count);
    }

    /**
     * testNonSyncIncrement
     */
    @Test
    public void demoB() {
        count = 0;

        IntStream.range(0, NUM_INCREMENTS).forEach(i -> executor.submit(SynchronizedDemo::increment));

        Threads.stop(executor);

        System.err.println("NonSync: " + count);
    }

    /**
     * testSyncIncrement
     */
    @Test
    public void demoC() {
        count = 0;

        IntStream.range(0, NUM_INCREMENTS).forEach(i -> executor.submit(SynchronizedDemo::incrementSyncClass));

        Threads.stop(executor);

        System.err.println(count);
    }


}