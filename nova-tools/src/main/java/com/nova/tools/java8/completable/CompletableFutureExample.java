package com.nova.tools.java8.completable;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @description: 一.什么是CompletableFuture?
 * 通俗的理解，它一个线程任务编排工具类
 * 他里面提供不同的静态方法让开发人员比较方便的去提交任务和组织任务的执行关系以及聚合任务执行结果等等
 * <p>
 * 二.为什么有CompletableFuture？降低了并发编程的复杂度
 * 在JDK8之前我们使用多线程的大部分情况都是Thead+Runnable或者使用Thread + Callable等方法来完成。
 * 如果我们需要多个并发任务同时执行，并且又关系他们的执行同步关系的话可能会在加上CountDownLatch、*CyclicBarrier等线程同步工具，这样就会导致并发编程变得比较复杂。
 * 而CompletableFuture帮我们封装很多任务提交、同步方法，让我们可以很轻松简单的来完成任务的提交、同步编排。
 * @author: wzh
 * @date: 2022/11/18 13:41
 */
@Slf4j
public class CompletableFutureExample {

    public static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(
            4, 6, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(512),
            new ThreadFactoryBuilder().setNamePrefix("completableFuture").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static String getThreadName() {
        return Thread.currentThread().getName();
    }

    /**
     * -----------------------------创建异步任务-----------------------------
     * </p>
     * runAsync：没返回
     * supplyAsync：有返回
     */
    @Test
    public void asyncTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
            System.err.println("ThreadName() = " + CompletableFutureExample.getThreadName());
            System.err.println("runAsync。。。");
        }, POOL);

        CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.err.println("ThreadName() = " + CompletableFutureExample.getThreadName());
            System.err.println("supplyAsync。。。");
            return 2333;
        }, POOL);

        //阻塞拿结果
        Integer result = supplyAsync.get();
        System.err.println("result = " + result);
    }

    /**
     * -----------------------------异步回调处理-----------------------------
     * </p>
     * 备注：前一个异步任务执行完，然后执行本任务
     * thenApply和thenApplyAsync（类型：function，有入参有返回）
     */
    @Test
    public void thenApplyTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.err.println("ThreadName() = " + CompletableFutureExample.getThreadName());
            System.err.println("supplyAsync。。。");
            return 2333;
        }, POOL);

        //与上面task共用一个线程池
        CompletableFuture<Integer> thenApply = supplyAsync.thenApply(integer -> {
            System.err.println("thenApply。。。");
            System.err.println("integer = " + integer);
            return integer * 2;
        });

        //todo 不传线程池，用共享线程forkJoin执行，凡是带有thenXxAsync都是一样
        CompletableFuture<Integer> thenApplyAsync = supplyAsync.thenApplyAsync(integer -> {
            System.err.println("ThreadName() = " + CompletableFutureExample.getThreadName());
            System.err.println("integer = " + integer);
            return integer * 2;
        }, POOL);

        Integer result = thenApply.get();
        System.err.println(result);

        Integer result2 = thenApplyAsync.get();
        System.err.println(result2);
    }

    /**
     * thenAccept和thenAcceptAsync（类型：function，有入参没返回）
     */
    @Test
    public void thenAcceptTest() {
        CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.err.println("ThreadName() = " + CompletableFutureExample.getThreadName());
            System.err.println("supplyAsync。。。");
            return 2333;
        }, POOL);

        //传入supplyAsync结果，但是自己没有返回
        CompletableFuture<Void> thenAccept = supplyAsync.thenAcceptAsync(integer -> {
            System.err.println("ThreadName() = " + CompletableFutureExample.getThreadName());
            System.err.println(integer);
        }, POOL);

        //等待一下，查看是否执行完
        thenAccept.join();
        System.err.println("isDone = " + thenAccept.isDone());
    }

    /**
     * thenRun和thenRunAsync（没入参，没返回）
     */
    @Test
    public void thenRunTest() {
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
            System.err.println("ThreadName() = " + CompletableFutureExample.getThreadName());
            System.err.println("runAsync。。。");
        }, POOL);

        CompletableFuture<Void> thenRunAsync = runAsync.thenRunAsync(() -> {
            System.err.println("thenRun（没入参，没返回）");
            System.err.println("thenRunAsync。。。");
        }, POOL);
    }

    /**
     * todo 我的理解 whenComplete = thenApply + exceptionally
     * whenComplete和whenCompleteAsync
     * 传入两个参数（结果值，异常）
     * 无自己的返回
     */
    @Test
    public void whenCompleteTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.err.println("supplyAsync。。。");
            int a = 1 / 0;
            return 2333;
        });

        CompletableFuture<Integer> whenCompleteAsync = supplyAsync.whenCompleteAsync((integer, e) -> {
            System.out.println("integer = " + integer);
            System.out.println("e = " + e);
        }, POOL);

        //todo 注意结果是supplyAsync返回的 2333，不是whenCompleteAsync的
        Integer result = whenCompleteAsync.get();
        System.out.println("result = " + result);
    }


    /**
     * handle和handleAsync
     * 和whenComplete用法一致，区别：有返回值
     */
    @Test
    public void handleTest() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.err.println("supplyAsync。。。");
            return 2333;
        });

        CompletableFuture<Integer> handleAsync = supplyAsync.handleAsync((integer, e) -> {
            System.err.println("integer = " + integer);
            System.err.println("e = " + e);
            return integer * 2;
        }, POOL);

        Integer result = handleAsync.get();
        System.err.println("result = " + result);
    }


    /**
     * -----------------------------多任务组合处理-----------------------------
     * thenCombine和thenCombineAsync
     * taskA和taskB都执行完，开始组合结果，有返回值
     */
    @Test
    public void thenCombineTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            System.err.println("taskA。。。");
            return "Hello";
        }, POOL);

        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> {
            System.err.println("taskB。。。");
            return "World";
        }, POOL);

        CompletableFuture<String> thenCombine = taskA.thenCombineAsync(taskB, (a, b) -> {
            System.err.println("thenCombine。。。");
            return a + b;
        }, POOL);

        String result = thenCombine.get();
        System.err.println("result = " + result);
    }

    /**
     * 与thenCombine相似，区别：没有返回值
     */
    @Test
    public void thenAcceptBothTest() {
        CompletableFuture<Integer> taskA = CompletableFuture.supplyAsync(() -> {
            System.err.println("taskA。。。");
            return 1;
        }, POOL);

        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> {
            System.err.println("taskB。。。");
            return "个世界";
        }, POOL);

        CompletableFuture<Void> thenAcceptBoth = taskA.thenAcceptBothAsync(taskB, (a, b) -> {
            System.err.println("thenAcceptBoth。。。");
            System.err.println("result = " + a + b);
        }, POOL);
    }

    /**
     * runAfterBoth和runAfterBothAsync
     * 没有入参，没有返回值。taskA和taskB出现异常，则抛出该异常
     */
    @Test
    public void runAfterBothTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            System.err.println("taskA。。。");
            int a = 1 / 0;
            return "Hello";
        }, POOL);

        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> {
            System.err.println("taskB。。。");
            return "World";
        }, POOL);

        CompletableFuture<Void> runAfterBoth = taskA.runAfterBothAsync(taskB, () -> {
            System.err.println("runAfterBoth。。。");
        }, POOL);

        Void unused = runAfterBoth.get();
        System.err.println("unused = " + unused);
    }


    /**
     * applyToEither和applyToEitherAsync
     * 两个CompletableFuture组合起来处理，当任意一个任务正常完成时，就会进行下阶段任务
     */
    @Test
    public void applyToEitherTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            System.err.println("taskA。。。");
            ThreadUtil.sleep(2000);
            return "Hello";
        }, POOL);

        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> {
            System.err.println("taskB。。。");
            ThreadUtil.sleep(3000);
            return "World";
        }, POOL);

        CompletableFuture<String> taskC = taskA.applyToEitherAsync(taskB, (result) -> {
            System.err.println("taskC。。。");
            System.err.println("接收到" + result);
            return "taskC 任务完成";
        }, POOL);

        String result = taskC.get();
        System.err.println("result = " + result);
    }

    /**
     * 类似，有入参无返回
     */
    @Test
    public void acceptEitherTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(2000);
            System.err.println("taskA。。。");
            return "Hello";
        }, POOL);

        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(3000);
            System.err.println("taskB。。。");
            return "World";
        }, POOL);

        CompletableFuture<Void> taskC = taskA.acceptEitherAsync(taskB, (result) -> {
            System.err.println("taskC。。。");
            System.err.println("接收到" + result);
        }, POOL);

        Void unused = taskC.get();
        System.err.println("unused = " + unused);
    }


    /**
     * 类似，无入参无返回
     */
    @Test
    public void runAfterEitherTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(2000);
            System.err.println("taskA。。。");

            return "Hello";
        }, POOL);

        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(3000);
            System.err.println("taskB。。。");
            return "World";
        }, POOL);

        CompletableFuture<Void> taskC = taskA.runAfterEitherAsync(taskB, () -> {
            System.err.println("taskC。。。");
        }, POOL);

        Void unused = taskC.get();
        System.err.println("unused = " + unused);
    }

    /**
     * 都完成
     * todo join方法执行 main线程进行阻塞，等待都完成，相当于CountDownLatch.await()
     */
    @Test
    public void allOfTest() {
        CompletableFuture<Void> taskA = CompletableFuture.runAsync(() -> {
            ThreadUtil.sleep(2000);
            System.err.println("taskA。。。");
            throw new RuntimeException("taskA异常");
        }, POOL);

        CompletableFuture<Void> taskB = CompletableFuture.runAsync(() -> {
            ThreadUtil.sleep(3000);
            System.err.println("taskB。。。");
//            throw new RuntimeException("taskB异常");
        }, POOL);

        CompletableFuture.allOf(taskA, taskB).thenRun(() -> {
            System.err.println("都完成");
        }).exceptionally(e -> {
            System.err.println("e = " + e.getCause().getMessage());
            throw new RuntimeException(e.getCause().getMessage());
        }).join();
    }


    /**
     * 任意一个任务完成
     */
    @Test
    public void anyOfTest() {
        CompletableFuture<Void> taskA = CompletableFuture.runAsync(() -> {
            ThreadUtil.sleep(1000);
            System.err.println("taskA。。。");
        }, POOL);

        CompletableFuture<Void> taskB = CompletableFuture.runAsync(() -> {
            ThreadUtil.sleep(3000);
            System.err.println("taskB。。。");
        }, POOL);

        CompletableFuture.anyOf(taskA, taskB).join();
        System.err.println("任意完成");
    }

    /**
     * exceptionally，处理异常
     */
    @Test
    public void exceptionallyTest() throws ExecutionException, InterruptedException {
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            System.err.println("taskA。。。");
//            int a = 1 / 0;
            return "taskA";
        }, POOL);

        taskA.thenAccept(s -> {
            int a = 1 / 0;
            System.out.println("thenAccept = " + s);
        });

        taskA.exceptionally(e -> {
            System.err.println("exceptionally");
            System.err.println("e = " + e);
            return "exceptionally";
        });

        String result = taskA.get();
        System.err.println("result = " + result);
    }


    /**
     * 循环打印A，B，C
     */
    @Test
    public void circularPrintlnTest() {
        List<CompletableFuture<String>> completableFutures = new ArrayList<>();
        CompletableFuture<String> printA = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(500);
            return "A";
        });
        CompletableFuture<String> printB = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(1500);
            return "B";
        });
        CompletableFuture<String> printC = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(2500);
            return "C";
        });

        completableFutures.add(printA);
        completableFutures.add(printB);
        completableFutures.add(printC);

        CompletableFuture.anyOf(completableFutures.toArray(new CompletableFuture[0])).join();
        for (CompletableFuture<String> result : completableFutures) {
            try {
                String s = result.get();
                System.err.println("打印 = " + s);
            } catch (Exception ignored) {

            }
        }
    }


}

