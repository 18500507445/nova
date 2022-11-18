package com.nova.tools.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @Description: 原子变量 AtomicInteger,LongAdder,LongAccumulator
 * @Author: wangzehui
 * @Date: 2022/11/18 14:49
 */
public class AtomicExample {

    private static final int NUM_INCREMENTS = 1000;

    private static AtomicInteger ATOMIC_INT = new AtomicInteger(0);

    @Test
    public void testUpdate() {
        ATOMIC_INT.set(0);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> {
                    Runnable task = () ->
                            ATOMIC_INT.updateAndGet(n -> n + 2);
                    executor.submit(task);
                });

        ConcurrentUtils.stop(executor);

        System.out.format("Update: %d\n", ATOMIC_INT.get());
    }

    @Test
    public void testAccumulate() {
        ATOMIC_INT.set(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> {
                    Runnable task = () ->
                            ATOMIC_INT.accumulateAndGet(i, (n, m) -> n + m);
                    executor.submit(task);
                });

        ConcurrentUtils.stop(executor);

        System.out.format("Accumulate: %d\n", ATOMIC_INT.get());
    }

    @Test
    public void testIncrement() {
        ATOMIC_INT.set(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> executor.submit(ATOMIC_INT::incrementAndGet));

        ConcurrentUtils.stop(executor);

        System.out.format("Increment: Expected=%d; Is=%d\n", NUM_INCREMENTS, ATOMIC_INT.get());
    }
}
