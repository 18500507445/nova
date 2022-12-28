package com.nova.tools.java8.completablefuture;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.NumberUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @description: 一.什么是CompletableFuture?
 * 通俗的理解，CompletableFuture其实是一个线程任务编排工具类，
 * 他里面提供不同的静态方法让开发人员比较方便的去提交任务和组织任务的执行关系以及聚合任务执行结果等等，
 * 他是在JDK1.8之后才有的(包含JDK1.8)。所以在这里你先记着他是一个工具类。
 * <p>
 * 二.为什么有CompletableFuture
 * 降低了并发编程的复杂度。
 * 在JDK8之前我们使用多线程的大部分情况都是Thead+Runnable或者使用Thread + Callable等方法来完成。* 如果我们需要多个并发任务同时执行，并且又关系他们的执行同步关系的话可能会在加上CountDownLatch、*CyclicBarrier等线程同步工具，这样就会导致并发编程变得比较复杂。
 * 而CompletableFuture帮我们封装很多任务提交、同步方法，让我们可以很轻松简单的来完成任务的提交、同步编排。
 * @author: wangzehui
 * @date: 2022/11/18 13:41
 */
public class CompletableFutureExample {

    /**
     * 可以自定义线程池或使用默认线程池对数据进行异步处理，且可以根据需求选择是否返回异步结果！灵活的使用
     */
    @Test
    public void demoA() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> submit = executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(3);
            return 100;
        });
        try {
            Integer result = submit.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        while (!submit.isDone()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Integer result = submit.get();
        System.out.println(result);
    }

    /**
     * 处理计算结果
     * <p>
     * 任务提交类型
     * runAsync：没有返回结果
     * supplyAsync：有返回结果
     * 并且两个都可以自定义线程池去执行
     */
    @Test
    public void demoB() {
        CompletableFuture<Void> helloFuture = CompletableFuture.runAsync(() -> System.out.println("hello future"));

        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> 2333);
    }

    /**
     * 结果转换
     */
    @Test
    public void demoC() {
        CompletableFuture<Integer> uCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("开始执行运算");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            System.out.println("执行结束");
            return 2333;
        });

        try {
            Integer result = uCompletableFuture.whenComplete((a, b) -> {
                System.out.println("Result: " + a);
                System.out.println("Exception: " + b);
            }).exceptionally(e -> {
                System.out.println(e.getMessage());
                return 666;
            }).get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扁平转换
     * <p>
     * 任务依赖关系编排类型：一个任务执行依赖另外一个任务的执行结果
     * thenApply()
     * theCompose()
     * <p>
     * 相同点：两者都是用于连接多个CompletableFuture调用
     * <p>
     * 不同点：thenApply()接受一个函数作为参数，使用该函数处理上一个CompletableFuture调用的结果
     * theCompose的参数为一个返回CompletableFuture实例的函数，该函数的参数是先前计算步骤的结果
     */
    @Test
    public void demoD() {
        try {
            String result = CompletableFuture.supplyAsync(() -> 2333).thenApply(String::valueOf).get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消费结果
     */
    @Test
    public void demoE() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> 9999).thenAccept(System.out::println).get();
    }

    /**
     * 消费结果处理
     */
    @Test
    public void demoF() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> 9999)
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> "7878"), (a, b) -> {
                    System.out.println("a = " + a);
                    System.out.println("b = " + b);
                }).get();
    }

    @Test
    public void demoG() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("开始执行了");
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 9999;
        }).thenRun(() -> {
            System.out.println("执行结束了");
        }).get();
    }


    /**
     * 两个任务之间执行and关系；两个任务并行执行完成以后对其结果进行聚合返回
     * thenCombine：聚合结果有返回值
     * thenAcceptBoth：聚合结果没有返回值
     * runAfterBoth：两个任务都执行完成以后执行行的runnable方法
     */
    @Test
    public void demoH() {
        try {
            String s = CompletableFuture.supplyAsync(() -> 23333)
                    .thenCombine(CompletableFuture.supplyAsync(() -> "8898"), (a, b) -> {
                        System.out.println("a =" + a);
                        System.out.println("b =" + b);
                        return a + b;
                    })
                    .get();
            System.out.println(s);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void demoI() throws ExecutionException, InterruptedException {
        m1().acceptEither(m2(), t -> {
            System.out.println("t = " + t);
        }).get();
    }


    /**
     * anyOf:m1和m2任务只要有一个异步线程完成就触发
     * allOf：都完成再触发
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void demoJ() throws ExecutionException, InterruptedException {
        TimeInterval timer = DateUtil.timer();
        CompletableFuture.anyOf(m1(), m2())
                .thenRun(() -> {
                    System.out.println("anyOf：" + timer.interval());
                }).get();

        timer.restart();
        CompletableFuture.allOf(m1(), m2())
                .thenRun(() -> {
                    System.out.println("allOf：" + timer.interval());
                }).get();
    }

    /***
     * 多个线程 数据合并取结果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void demoK() throws ExecutionException, InterruptedException {
        TimeInterval timer = DateUtil.timer();
        CompletableFuture<Integer> a = CompletableFuture.supplyAsync(() -> {
            int sum = 0;
            for (int i = 0; i < 10000; i++) {
                sum = sum + i;
            }
            return sum;
        });
        CompletableFuture<Integer> b = CompletableFuture.supplyAsync(() -> {
            int sum = 0;
            for (int i = 10000; i < 20000; i++) {
                sum = sum + i;
            }
            return sum;
        });
        CompletableFuture.allOf(a, b).join();
        int one = a.get();
        int two = b.get();
        System.out.println("结果：" + NumberUtil.add(one + two) + "，耗时：" + timer.interval() + "ms");

        timer.restart();
        int sum = 0;
        for (int i = 0; i < 20000; i++) {
            sum = sum + i;
        }
        System.out.println("结果：" + sum + "，耗时：" + timer.interval() + "ms");

    }

    private static CompletableFuture<Integer> m1() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2333;
        });
    }

    private static CompletableFuture<Integer> m2() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 8877;
        });
    }


}

