package com.nova.tools.demo.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @date:Created in 17:34 2018/10/30
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        // 结果集
        List<String> list = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Integer> taskList = Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);

        // 全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
        BiConsumer<String, Throwable> biConsumer = new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable e) {
                System.out.println("任务" + s + "完成!result=" + s + "，异常 e=" + e + "," + new Date());
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
        CompletableFuture[] completableFutures = objectStream.toArray(intFunction);
        CompletableFuture[] cfs = completableFutures;
        // 封装后无返回值，必须自己whenComplete()获取
        CompletableFuture.allOf(cfs).join();

        for (Integer i : taskList) {
            list.add(calc(i).toString());
        }
        System.out.println("list=" + list + ",耗时=" + ((System.currentTimeMillis() - start) / 1000 + "s"));
    }

    public static Integer calc(Integer i) {
        try {
            if (i == 1) {
                //任务1耗时3秒
                Thread.sleep(3000);
            } else if (i == 5) {
                //任务5耗时5秒
                Thread.sleep(5000);
            } else {
                //其它任务耗时1秒
                Thread.sleep(1000);
            }
            System.out.println("task线程：" + Thread.currentThread().getName()
                    + "任务i=" + i + ",完成！+" + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }
}


