package com.nova.tools.demo.thread;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: wzh
 * @description: threadLocal传值问题
 * @date: 2023/11/19 21:00
 */
public class ThreadLocalTest {

    /**
     * tl
     */
    private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * itl
     */
    private static final ThreadLocal<Integer> I_THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * ttl
     */
    private static final ThreadLocal<Integer> T_THREAD_LOCAL = new TransmittableThreadLocal<>();


    /**
     * 测试threadLocal，结果，sub线程并不能获取到主线程值
     */
    @Test
    public void demoTL() {
        THREAD_LOCAL.set(1);
        System.err.println("TL main线程：" + THREAD_LOCAL.get());

        CompletableFuture.runAsync(() -> System.err.println("TL sub线程：" + THREAD_LOCAL.get()));

        ThreadUtil.sleep(1000);
        System.err.println("============================================");

        THREAD_LOCAL.set(2);
        System.err.println("TL main线程：" + THREAD_LOCAL.get());
        CompletableFuture.runAsync(() -> System.err.println("TL sub线程：" + THREAD_LOCAL.get()));
    }


    /**
     * 测试IThreadLocal，结果：2和3sub线程，可以获取主线程值，但都是1，说明获取的都是第一子线程的值，复制值了。
     */
    @Test
    public void demoITL() {
        I_THREAD_LOCAL.set(1);
        System.err.println("ITL main线程：" + I_THREAD_LOCAL.get());

        CompletableFuture.runAsync(() -> System.err.println("ITL sub线程：" + I_THREAD_LOCAL.get()));

        ThreadUtil.sleep(1000);
        System.err.println("============================================");

        I_THREAD_LOCAL.set(2);
        System.err.println("ITL main线程：" + I_THREAD_LOCAL.get());
        CompletableFuture.runAsync(() -> System.err.println("ITL sub线程：" + I_THREAD_LOCAL.get()));

        I_THREAD_LOCAL.set(3);
        System.err.println("ITL main线程：" + I_THREAD_LOCAL.get());
        CompletableFuture.runAsync(() -> System.err.println("ITL sub线程：" + I_THREAD_LOCAL.get()));
    }

    /**
     * 进一步验证，itl的值串行了。
     */
    @Test
    public void demoITL2() {
        for (int i = 0; i < 10; i++) {
            I_THREAD_LOCAL.set(i);
            System.err.println("ITL main线程：" + I_THREAD_LOCAL.get());
            ThreadUtil.execAsync(() -> System.err.println("ITL sub线程：" + I_THREAD_LOCAL.get()));
        }
    }


    /**
     * 测试ttl传值
     */
    @Test
    public void demoTTL() {
        //使用ttl包装JDK线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Executor ttlExecutor = TtlExecutors.getTtlExecutor(executorService);

        for (int i = 0; i < 10; i++) {
            T_THREAD_LOCAL.set(i);
            System.err.println("TTL main线程：" + T_THREAD_LOCAL.get());
            ttlExecutor.execute(() -> System.err.println("TTL sub线程：" + T_THREAD_LOCAL.get()));
        }

    }


}
