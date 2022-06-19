package com.nova.tools.demo.thread;


import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.nova.tools.utils.time.DateUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @Description:  问题:为什么使用线程池：创建和销毁线程耗费大量的时间,效率很低 为了提高线程的利用率使用线程池
 * @Author: wangzehui
 * @Date: 2021/3/31 11:13
 * web:https://www.freesion.com/article/32671046670/
 */
public class ManualCreate {

    private static final Logger logger = LoggerFactory.getLogger(ManualCreate.class);

    //获取机器核数，作为线程池数量
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    private static final ThreadFactory NAME_THREAD_FACTORY = new ThreadFactoryBuilder().build();

    /**
     * Semaphore也是一个线程同步的辅助类，可以维护当前访问自身的线程个数，并提供了同步机制。使用Semaphore可以控制同时访问资源的线程个数
     * 例如，实现一个文件允许的并发访问数。
     */
    private final Semaphore SEMAPHORE = new Semaphore(THREAD_COUNT);

    /**
     * 获取当前机器ip地址
     * private static final String ip = Inet4Address.getLocalHost().getHostAddress();
     */
    private static String IP = "";

    static {
        try {
            IP = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /*
     * corePoolSize => 线程池核心线程数量
     * maximumPoolSize => 线程池最大数量,必须大于等于1
     * keepAliveTime => 空闲线程存活时间,当空闲时间到达此值,多余线程会被销毁到核心线程数量
     * unit => 时间单位
     * workQueue => 线程池所使用的缓冲队列,里面放了被提交但尚未被执行的任务
     * threadFactory => 线程池创建线程使用的工厂
     * handler => 拒绝策略，当线程池最大数和队列都满了，对任务的拒绝方式
     * (1)AbortPolicy:默认策略 抛出异常
     * (2)DiscardPolicy:丢弃任务
     * (3)DiscardOldestPolicy:丢弃队列最前面任务,执行该任务
     * (4)CallerRunsPolicy:是不会丢弃任务，直接运行任务的run方法，即使用主线程执行任务
     */
    private static final ExecutorService SERVICE = new ThreadPoolExecutor(THREAD_COUNT, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), NAME_THREAD_FACTORY, new ThreadPoolExecutor.CallerRunsPolicy());

    private static final BlockingQueue<Integer> QUEUE = new LinkedBlockingQueue<>();

    @PostConstruct
    public void init() {
        start();
        process();
    }

    public void start() {
        try {
            if (StringUtils.isBlank(IP)) {
                logger.error("未获取到主机ip地址");
                return;
            }
            //todo 可做解锁ip处理 情景举例：一个任务执行失败了，扔到重试表里，重试表有个locker字段，定时任务一直扫表，哪台机器哪个线程抢到任务locker放ip，然后当任务被线程处理，locker清除ip
            System.out.println("模拟:清除该ip锁定的任务后,睡眠2s");
            Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            return;
        }
        System.out.println("------------准备处理业务------------");
        SERVICE.submit(new RetryTask());
    }

    public void process() {
        for (int i = 0; i < THREAD_COUNT; i++) {
            SERVICE.submit(new Task());
        }
    }

    public static void main(String[] args) {

    }

    class RetryTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    //返回此信号灯中可用的许可证数量
                    int taskNum = SEMAPHORE.availablePermits();
                    System.out.println("------------允许TASK个数：" + taskNum);

                    if (taskNum == 0) {
                        Thread.sleep(1000);
                        continue;
                    }

                    if (taskNum > 0) {
                        //todo 比如根据taskNum 锁定重试任务表 where locker = ip limit taskNum
                        System.out.println("获取业务，处理业务的机器为：" + IP + "时间:" + DateUtil.parse(new Date()));
                    }

                    //QUEUE.put() 处理实体类
                    for (int i = 0; i < taskNum; i++) {
                        SEMAPHORE.acquire();
                        QUEUE.put(i);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                } finally {
                    logger.debug("休眠时间:" + DateUtil.parse(new Date()));
                }
            }
        }
    }


    /**
     * 业务处理
     */
    class Task implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Integer take = QUEUE.take();
                    consume(take);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //处理业务完成 释放信号
    private void consume(Integer i) {
        try {
            System.out.println("处理次数" + i);
        } catch (Exception exp) {
            logger.error("重试异常" + exp);
        } finally {
            SEMAPHORE.release();
        }
    }


}


