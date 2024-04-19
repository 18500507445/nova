package com.nova.book.juc.chapter7.section2;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @description: 线程池demo
 * @author: wzh
 * @date: 2023/3/30 13:33
 */
@Slf4j(topic = "Executor")
class Executor {

    static ExecutorService single = Executors.newSingleThreadExecutor();

    static ExecutorService pool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        //execute();

        //submit();

        //invokeAll();

        //invokeAny();

        shutDown();

    }

    /**
     * 自己创建一个单线程串行执行任务，如果任务执行失败而终止那么没有任何补救措施，而线程池还会新建一个线程，保证池的正常工作
     */
    public static void execute() {
        single.execute(() -> {
            log.debug("1");
            int i = 1 / 0;
        });

        single.execute(() -> {
            log.debug("2");
        });

        single.execute(() -> {
            log.debug("3");
        });
    }

    /**
     * 提交返回future
     */
    public static void submit() {
        final Future<String> future = pool.submit(() -> {
            log.debug("Callable Submit");
            ThreadUtil.sleep(1000);
            return "OK";
        });

        try {
            log.debug("{}", future.get());
        } catch (InterruptedException | ExecutionException e) {
            log.error("异常：", e);
        }
    }

    /**
     * 返回所有结果
     */
    public static void invokeAll() {
        try {
            List<Future<String>> futures = pool.invokeAll(Arrays.asList(
                    () -> {
                        log.debug("begin1");
                        ThreadUtil.sleep(1000);
                        return "1";
                    },
                    () -> {
                        log.debug("begin2");
                        ThreadUtil.sleep(500);
                        return "2";
                    },
                    () -> {
                        log.debug("begin3");
                        ThreadUtil.sleep(2000);
                        return "3";
                    }
            ));

            futures.forEach(f -> {
                try {
                    log.debug("结果：{}", f.get());
                } catch (InterruptedException | ExecutionException e) {
                    log.error("异常：", e);
                }
            });
        } catch (InterruptedException e) {
            log.error("异常：", e);
        }
    }

    /**
     * 返回最快的一个结果
     */
    public static void invokeAny() {
        String result = null;
        try {
            result = pool.invokeAny(Arrays.asList(
                    () -> {
                        log.debug("begin 1");
                        Thread.sleep(1000);
                        log.debug("end 1");
                        return "1";
                    },
                    () -> {
                        log.debug("begin 2");
                        Thread.sleep(500);
                        log.debug("end 2");
                        return "2";
                    },
                    () -> {
                        log.debug("begin 3");
                        Thread.sleep(2000);
                        log.debug("end 3");
                        return "3";
                    }
            ));
        } catch (InterruptedException | ExecutionException e) {
            log.error("异常：", e);
        }
        log.debug("先执行完的结果：{}", result);
    }

    public static void shutDown() {
        Future<Integer> result1 = pool.submit(() -> {
            log.debug("task 1 running...");
            ThreadUtil.sleep(1000);
            log.debug("task 1 finish...");
            return 1;
        });

        Future<Integer> result2 = pool.submit(() -> {
            log.debug("task 2 running...");
            ThreadUtil.sleep(1000);
            log.debug("task 2 finish...");
            return 2;
        });

        Future<Integer> result3 = pool.submit(() -> {
            log.debug("task 3 running...");
            ThreadUtil.sleep(1000);
            log.debug("task 3 finish...");
            return 3;
        });

        log.debug("shutdown");

        pool.shutdown();

        //pool.awaitTermination(3, TimeUnit.SECONDS);

        //log.debug("other.... {}", pool.shutdownNow());
    }
}
