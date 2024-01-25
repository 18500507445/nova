package com.nova.tools.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @description: 原子变量 AtomicInteger,LongAdder,LongAccumulator
 * @author: wzh
 * @date: 2022/11/18 14:49
 */
class AtomicExample {

    private static final int NUM_INCREMENTS = 1000;

    private static final AtomicInteger ATOMIC_INT = new AtomicInteger(0);

    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    @Test
    public void testUpdate() {
        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> {
                    Runnable task = () -> ATOMIC_INT.updateAndGet(n -> n + 2);
                    executor.submit(task);
                });

        executor.shutdown();
        System.err.format("Update: %d\n", ATOMIC_INT.get());
    }

    @Test
    public void testAccumulate() {
        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> {
                    Runnable task = () ->
                            ATOMIC_INT.accumulateAndGet(i, (n, m) -> n + m);
                    executor.submit(task);
                });

        executor.shutdown();

        System.err.format("Accumulate: %d\n", ATOMIC_INT.get());
    }

    @Test
    public void testIncrement() {
        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> executor.submit(ATOMIC_INT::incrementAndGet));

        executor.shutdown();

        System.err.format("Increment: Expected=%d; Is=%d\n", NUM_INCREMENTS, ATOMIC_INT.get());
    }
}
