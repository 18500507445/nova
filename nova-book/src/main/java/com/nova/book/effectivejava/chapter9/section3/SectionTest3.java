package com.nova.book.effectivejava.chapter9.section3;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description: 线程池工厂
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
@Slf4j(topic = "SectionTest3")
class SectionTest3 {

    /**
     * 固定大小，核心线程数为8，最大线程数8，队列为无界队列，不会主动停止（main方法里执行）
     * <p>
     * 场景：任务量已知，相对耗时的任务
     */
    @Test
    public void demoA() {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        executorService.execute(() -> log.debug("1"));

        executorService.execute(() -> log.debug("2"));

        executorService.execute(() -> log.debug("3"));
    }

    /**
     * 单线程的线程池，等价于newFixedThreadPool(1)，一个核心线程没有救急线程
     */
    @Test
    public void demoB() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * 核心线程数0，最大线程数Integer.MAX_VALUE，救急线程存活60s(最后一次任务执行完毕60s后没有开启新的工作，则回收线程)，救急线程可以无线创建
     * <p>
     * 场景：一手交钱，一手交货
     */
    @Test
    public void demoC() {
        System.out.println(Integer.MAX_VALUE);
        ExecutorService executorService = Executors.newCachedThreadPool();
    }

    /**
     * 周期性执行任务的线程池
     */
    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        log.debug("开始");
        //延时执行
        pool.schedule(() -> {
            log.debug("task1");
        }, 1, TimeUnit.SECONDS);

        pool.schedule(() -> {
            log.debug("task2");
        }, 1, TimeUnit.SECONDS);

        //定时执行
        pool.scheduleAtFixedRate(() -> {
            log.debug("task3");
        }, 1, 1, TimeUnit.SECONDS);
    }

}
