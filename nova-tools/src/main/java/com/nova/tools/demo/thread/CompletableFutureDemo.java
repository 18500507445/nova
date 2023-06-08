package com.nova.tools.demo.thread;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.nova.common.utils.thread.Threads;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description: CompletableFutureDemo
 * @author: wzh
 */
class CompletableFutureDemo {

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
                System.out.println("任务" + s + "完成!result=" + s + "，异常e =" + e + "," + DateUtil.now());
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
        System.out.println("list=" + list + ",耗时=" + timer.interval() + "ms");
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
            System.out.println("task线程：" + Thread.currentThread().getName() + "，任务i= " + i + "，完成时间点：" + DateUtil.now());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }


    private static final TimeInterval TIMER = DateUtil.timer();

    /**
     * 只要有一个异步线程完成就立刻返回结果
     */
    @Test
    public void testSync() {
        List<Integer> urlList = CollUtil.newArrayList(1, 2, 3, 4, 5, 6);
        List<Integer> randomList = RandomUtil.randomEleList(urlList, 3);

        Object result = CompletableFuture.anyOf(randomList.stream().map(integer -> CompletableFuture.supplyAsync(() -> {
            Threads.sleep(integer * 1000);
            return integer;
        })).toArray(CompletableFuture[]::new)).join();

        System.out.println("耗时：" + TIMER.interval());
        System.out.println(result);

    }

    @Test
    public void testC() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        ArrayList<Integer> integers = new ArrayList<>();

        //supplyAsync 构造一个CompletableFuture
        //CompletableFuture.completedFuture() 构造一个已完成的future
        //thenApply 相当于回调函数
        List<CompletableFuture> completableFutures = list.stream().map(e -> CompletableFuture.supplyAsync(() -> {
            //异步任务执行 模拟耗时任务
            Threads.sleep(Convert.toInt(e) * 1000);
            integers.add(Convert.toInt(e));
            return e;
        }).thenApply(String::toUpperCase)).collect(Collectors.toList());
        //阻塞主线程直到所有全部任务完成
        CompletableFuture.anyOf(completableFutures.toArray(new CompletableFuture[0])).join();

        System.out.println(TIMER.interval());
        System.out.println(JSONUtil.toJsonStr(integers));

        completableFutures.forEach(e -> {
            try {
                //获得执行结果
                System.out.println("执行结果" + e.get());
            } catch (InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }
        });

    }


}


