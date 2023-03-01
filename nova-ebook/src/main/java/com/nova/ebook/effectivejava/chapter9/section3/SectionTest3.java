package com.nova.ebook.effectivejava.chapter9.section3;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest3 {

    /**
     * 固定大小，核心线程数为8，最大线程数8，队列为无界队列
     */
    @Test
    public void demoA() {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
    }

    /**
     * 单线程的线程池，等价于newFixedThreadPool(1)
     */
    @Test
    public void demoB() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * 核心线程数0，最大线程数Integer.MAX_VALUE，线程存活60s(最后一次任务执行完毕60s后没有开启新的工作，则回收线程)
     */
    @Test
    public void demoC() {
        System.out.println(Integer.MAX_VALUE);
        ExecutorService executorService = Executors.newCachedThreadPool();
    }

    /**
     * 周期性执行任务的线程池
     */
    @Test
    public void demoD() {
        ExecutorService executorService = Executors.newScheduledThreadPool(8);
    }

}
