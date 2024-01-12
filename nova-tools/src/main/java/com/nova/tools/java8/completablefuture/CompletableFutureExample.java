package com.nova.tools.java8.completablefuture;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @description: 一.什么是CompletableFuture?
 * 通俗的理解，CompletableFuture其实是一个线程任务编排工具类，
 * 他里面提供不同的静态方法让开发人员比较方便的去提交任务和组织任务的执行关系以及聚合任务执行结果等等，
 * 他是在JDK1.8之后才有的(包含JDK1.8)。所以在这里你先记着他是一个工具类。
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

    /**
     * 处理计算结果
     * <p>
     * 任务提交类型
     * runAsync：开启异步任务，没有返回结果
     * supplyAsync：开启异步任务，有返回结果
     * 并且两个都可以自定义线程池去执行
     */
    @Test
    public void demoA() {
        CompletableFuture<Void> helloFuture = CompletableFuture.runAsync(() -> System.err.println("hello future"));

        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> 2333);
    }

    /**
     * 结果转换
     */
    @Test
    public void demoB() {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.err.println("开始执行运算");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1 / 0;
            System.err.println("执行结束");
            return 2333;
        });

        try {
            Integer result = completableFuture.whenComplete((a, b) -> {
                System.err.println("Result: " + a);
                System.err.println("Exception: " + b);
            }).exceptionally(e -> {
                System.err.println(e.getMessage());
                return 666;
            }).get();
            System.err.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扁平转换
     * 任务依赖关系编排类型：一个任务执行依赖另外一个任务的执行结果
     * thenApply，任务后置处理，thenApply()接受一个函数作为参数，使用该函数处理上一个CompletableFuture调用的结果
     * thenAccept，任务后置处理，thenAccept()接受一个函数作为参数，不用return
     * theCompose，连接两个异步任务，theCompose的参数为一个返回CompletableFuture实例的函数，该函数的参数是先前计算步骤的结果
     * theCombine，合并两个异步任务
     * applyToEither，获取最先完成的任务
     * exceptionally，处理异常
     */
    @Test
    public void demoC() {
        try {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                System.err.println("supplyAsync = " + "2333");
                return 2333;
            }).thenApply(s -> {
                System.err.println("thenApply = " + "转成字符串后返回");
                return s + "";
            }).exceptionally(e -> {
                log.error("demoC: {}", e.getMessage());
                return null;
            });
            System.err.println(completableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消费结果
     */
    @Test
    public void demoD() throws ExecutionException, InterruptedException {
        //写法一
        CompletableFuture.supplyAsync(() -> 9999).thenAccept(System.err::println).get();

        //写法二
        CompletableFuture.supplyAsync(() -> {
            return 9999;
        }).thenAccept(s -> {
            System.err.println(s);
        }).get();
    }

    /**
     * 消费结果处理
     */
    @Test
    public void demoE() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> 9999)
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> "7878"), (a, b) -> {
                    System.err.println("a = " + a);
                    System.err.println("b = " + b);
                }).get();
    }

    /**
     * 无返回值的后置处理
     */
    @Test
    public void demoF() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            System.err.println("开始执行了");
            ThreadUtil.sleep(2000);
            return 9999;
        }).thenRun(() -> System.err.println("执行结束了")).get();
    }


    /**
     * 两个任务之间执行and关系；两个任务并行执行完成以后对其结果进行聚合返回
     * thenCombine：聚合结果有返回值
     * thenAcceptBoth：聚合结果没有返回值
     * runAfterBoth：两个任务都执行完成以后执行行的runnable方法
     */
    @Test
    public void demoG() {
        try {
            String s = CompletableFuture.supplyAsync(() -> 23333)
                    .thenCombine(CompletableFuture.supplyAsync(() -> "8898"), (a, b) -> {
                        System.err.println("a =" + a);
                        System.err.println("b =" + b);
                        return a + b;
                    }).get();
            System.err.println(s);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将已经完成任务的执行结果作为方法入参，但是无返回值
     * <p>
     * 区别：applyToEither会将已经完成任务的执行结果作为所提供函数的参数，且该方法有返回值；acceptEither同样将已经完成任务的执行结果作为方法入参，但是无返回值；runAfterEither没有入参，也没有返回值。
     */
    @Test
    public void demoH() throws ExecutionException, InterruptedException {
        m1().acceptEither(m2(), t -> {
            System.err.println("t = " + t);
        }).get();
    }


    /**
     * anyOf:m1和m2任务只要有一个异步线程完成就触发
     * allOf：都完成再触发
     */
    @Test
    public void demoI() throws ExecutionException, InterruptedException {
        TimeInterval timer = DateUtil.timer();
        CompletableFuture.anyOf(m1(), m2())
                .thenRun(() -> {
                    System.err.println("anyOf：" + timer.interval() + " ms");
                }).get();

        timer.restart();
        CompletableFuture.allOf(m1(), m2())
                .thenRun(() -> {
                    System.err.println("allOf：" + timer.interval() + " ms");
                }).get();
    }

    /***
     * 多个线程 数据合并取结果
     */
    @Test
    public void demoJ() throws ExecutionException, InterruptedException {
        TimeInterval timer = DateUtil.timer();
        CompletableFuture<Long> task1 = CompletableFuture.supplyAsync(() -> {
            long sum = 0;
            for (long i = 0; i < 5000000000L; i++) {
                sum = sum + i;
            }
            return sum;
        });
        CompletableFuture<Long> task2 = CompletableFuture.supplyAsync(() -> {
            long sum = 0;
            for (long i = 5000000000L; i < 10000000000L; i++) {
                sum = sum + i;
            }
            return sum;
        });
        CompletableFuture.allOf(task1, task2).join();
        Long one = task1.get();
        Long two = task2.get();
        System.err.println("结果：" + NumberUtil.add(one + two) + "，耗时：" + timer.interval() + "ms");
    }


    /**
     * 区分 allOf和anyOf
     * allOf就相当于CountDownLatch.countDown和await
     */
    @Test
    public void demoK() {
        for (int i = 0; i < 5; i++) {
            List<CompletableFuture<Integer>> completableFutures = new ArrayList<>();
            completableFutures.add(m1());
            completableFutures.add(m2());
            completableFutures.add(m3());
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
            for (CompletableFuture<Integer> result : completableFutures) {
                try {
                    System.err.println("price：" + result.get());
                    System.err.println("time：" + DateUtil.now());
                } catch (Exception ignored) {

                }
            }
        }
    }

    private static CompletableFuture<Integer> m1() {
        int i = 3333;
        return CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(i);
            return i;
        });
    }

    private static CompletableFuture<Integer> m2() {
        int i = 4444;
        return CompletableFuture.supplyAsync(() -> {
            try {
//                return 8877;
                ThreadUtil.sleep(i);
                throw new RuntimeException();
            } catch (Exception ignored) {

            }
            return 0;
        });
    }

    private static CompletableFuture<Integer> m3() {
        int i = 5555;
        return CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(i);
            return i;
        });
    }


    /**
     * demo
     *
     * @param args
     */
    public static void main(String[] args) {
        TimeInterval timer = DateUtil.timer();
        // 结果集
        List<String> list = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Integer> taskList = Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);

        // 全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
        BiConsumer<String, Throwable> biConsumer = new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable e) {
                System.err.println("任务" + s + "完成!result=" + s + "，异常e =" + e + "," + DateUtil.now());
                list.add(s);
            }
        };
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer h) {
                return Integer.toString(h);
            }
        };
        Function<Integer, Object> function1 = new Function<Integer, Object>() {
            @Override
            public Object apply(Integer integer) {
                return CompletableFuture.supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        return calc(integer);
                    }
                }, executorService).thenApply(function).whenComplete(biConsumer);
            }
        };
        IntFunction<CompletableFuture[]> intFunction = new IntFunction<CompletableFuture[]>() {
            @Override
            public CompletableFuture[] apply(int value) {
                return new CompletableFuture[10];
            }
        };
        Stream<Integer> stream = taskList.stream();
        Stream<Object> objectStream = stream.map(function1);
        CompletableFuture[] cfs = objectStream.toArray(intFunction);
        // 封装后无返回值，必须自己whenComplete()获取
        CompletableFuture.allOf(cfs).join();

        for (Integer i : taskList) {
            list.add(calc(i).toString());
        }
        System.err.println("list=" + list + ",耗时=" + timer.interval() + "ms");
        System.exit(0);
    }

    public static Integer calc(Integer i) {
        try {
            if (i == 1) {
                //任务1耗时3秒
                TimeUnit.SECONDS.sleep(3);
            } else if (i == 5) {
                //任务5耗时5秒
                TimeUnit.SECONDS.sleep(5);
            } else {
                //其它任务耗时1秒
                TimeUnit.SECONDS.sleep(1);
            }
            System.err.println("task线程：" + Thread.currentThread().getName() + "，任务i= " + i + "，完成时间点：" + DateUtil.now());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }


    /**
     * 循环打印A，B，C
     */
    @Test
    public void circularPrintln() {
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

