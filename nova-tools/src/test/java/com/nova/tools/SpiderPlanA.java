package com.nova.tools;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author: wzh
 * @description 方案A
 * @date: 2023/05/25 10:52
 */
@Slf4j(topic = "SpiderPlanA")
public class SpiderPlanA {

    /**
     * 计时器
     */
    private static final TimeInterval timer = DateUtil.timer();

    /**
     * 获取机器核数，作为线程池数量
     */
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 线程工厂
     */
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNamePrefix("spider").build();

    /**
     * 手动创建线程池
     */
    private static final ExecutorService EXECUTOR_POOL = new ThreadPoolExecutor(12, 12,
            30L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), THREAD_FACTORY, new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 模拟任务数量
     */
    private static final int COUNT = 200_000;

    private static final int PAGE_SIZE = 10_000;

    /**
     * 单纯的IO型，最佳线程数目 = （（线程等待时间+线程CPU时间）/线程CPU时间 ）* CPU数目
     * 线程等待（网络IO、磁盘IO），线程cpu计算
     * 理论720w数据，分片720，每片1w对应开启一个线程。单片，1w数据，平均每次请求0.5s，请求超时设置尽量短些到时间断开，失败重试策略（暂不考虑，可用mq做补偿，不要影响主task）
     * <p>
     * 本地测试
     * coreNum = 8，耗时：1min，次数：1000
     * coreNum = 50，耗时：10s，次数：1000
     * coreNum = 80，耗时：7.8s，次数：1000
     * coreNum = 100，耗时：7.7s，次数：1000
     * coreNum = 700，耗时：7.7s，次数：1000
     * <p>
     * <p>
     * 满足上述公式所以可以根据网络IO时间测算coreNum
     * <p>
     * 网络IO 500ms，临界值coreNum = 80
     * 耗时：13min，次数：100000
     * 耗时：130min，次数：1000000
     * 耗时：862min，次数：7200000 理论15.6h
     * <p>
     * 网络IO 1000ms，临界值coreNum = 160
     * 耗时：7.3s，次数：1000
     *
     * @param args
     */
    public static void main(String[] args) {
        String skuId = "100038004347";
        List<String> queryList = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            queryList.add(skuId);
        }
        List<List<String>> partition = ListUtil.partition(queryList, 12);
        LongAdder longAdder = new LongAdder();

        for (List<String> skuIds : partition) {
            EXECUTOR_POOL.submit(new Task(skuIds, longAdder));
        }
    }

    static class Task implements Runnable {
        /**
         * 每次1w条
         */
        private final List<String> skuIds;

        /**
         * 计数器
         */
        private final LongAdder longAdder;

        public Task(List<String> skuIds, LongAdder longAdder) {
            this.skuIds = skuIds;
            this.longAdder = longAdder;
        }

        @Override
        public void run() {
            for (String skuId : skuIds) {
                ThreadUtil.sleep(600);
                longAdder.increment();
                log.info("time：" + DateUtil.now() + "，耗时：" + timer.interval() + "ms，价格：0，次数：" + longAdder.longValue());
                //查询到价格后发送mq，利用消息队列解耦，提高吞吐
                sendMq(skuId, "0");
            }
        }

        /**
         * 模拟发送mq
         *
         * @param skuId
         * @param price
         */
        public void sendMq(String skuId, String price) {
            ThreadUtil.sleep(5);
        }
    }


}
