package com.nova.tools.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/11/18 15:05
 */
public class ExecutorsDemo {

    ExecutorService EXECUTOR_ONE = Executors.newFixedThreadPool(1);

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @Test
    private void demoA() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                String name = Thread.currentThread().getName();
                System.out.println("task finished: " + name);
            } catch (InterruptedException e) {
                System.err.println("task interrupted");
            }
        });
        stop(executor);
    }

    static void stop(ExecutorService executor) {
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("termination interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
    }

    @Test
    public void demoB() throws InterruptedException, ExecutionException, TimeoutException {
        Future<Integer> future = EXECUTOR_ONE.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });
        future.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void demoC() throws InterruptedException, ExecutionException {
        Future<Integer> future = EXECUTOR_ONE.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });
        EXECUTOR_ONE.shutdownNow();
        future.get();
    }

    @Test
    public void demoD() throws InterruptedException, ExecutionException {
        Future<Integer> future = EXECUTOR_ONE.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        System.out.println("future done: " + future.isDone());
        Integer result = future.get();
        System.out.println("future done: " + future.isDone());
        System.out.print("result: " + result);
        EXECUTOR_ONE.shutdownNow();
    }

    public void demoE() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                callable("task1", 2),
                callable("task2", 1),
                callable("task3", 3));

        String result = executor.invokeAny(callables);
        System.out.println(result);

        executor.shutdown();
    }

    public Callable<String> callable(String result, long sleepSeconds) {
        return () -> {
            TimeUnit.SECONDS.sleep(sleepSeconds);
            return result;
        };
    }

    public void demoF() throws InterruptedException {
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3");

        executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);

        executor.shutdown();
    }

    public void demoG() {
        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Scheduling: " + System.nanoTime());
            } catch (InterruptedException e) {
                System.err.println("task interrupted");
            }
        };

        executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);
    }

    public void demoH() {
        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
        int initialDelay = 0;
        int period = 1;
        executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
    }

    public void demoI() throws InterruptedException {
        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
        int delay = 3;
        ScheduledFuture<?> future = executor.schedule(task, delay, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1337);

        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining Delay: %sms\n", remainingDelay);
    }

}
