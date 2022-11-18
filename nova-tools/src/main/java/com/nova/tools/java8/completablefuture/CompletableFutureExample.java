package com.nova.tools.java8.completablefuture;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/11/18 13:41
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

    @Test
    public void demoI() throws ExecutionException, InterruptedException {
        m1().acceptEither(m2(), t -> {
            System.out.println("t = " + t);
        }).get();
    }

    @Test
    public void demoJ() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture.anyOf(m1(), m2())
                .thenRun(() -> {
                    System.out.println(System.currentTimeMillis() - start);
                }).get();
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
