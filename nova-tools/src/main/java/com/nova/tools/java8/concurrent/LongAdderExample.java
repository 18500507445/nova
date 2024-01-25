package com.nova.tools.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

public class LongAdderExample {

    private static final int NUM_INCREMENTS = 10000;

    /**
     * 累加器
     */
    private static final LongAdder adder = new LongAdder();


    /**
     * testAdd
     */
    @Test
    public void demoA() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS).forEach(i -> executor.submit(() -> adder.add(2)));

        executor.shutdown();

        System.err.format("Add: %d\n", adder.sumThenReset());
    }

    /**
     * testIncrement
     */
    @Test
    public void demoB() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS).forEach(i -> executor.submit(adder::increment));

        executor.shutdown();

        System.err.format("Increment: Expected=%d; Is=%d\n", NUM_INCREMENTS, adder.sumThenReset());
    }

    /**
     * testAccumulate
     */
    @Test
    public void demoC() {
        LongBinaryOperator op = (x, y) -> 2 * x + y;
        LongAccumulator accumulator = new LongAccumulator(op, 1L);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 10).forEach(i -> executor.submit(() -> accumulator.accumulate(i)));

        executor.shutdown();
        System.err.format("Add: %d\n", accumulator.getThenReset());
    }
}