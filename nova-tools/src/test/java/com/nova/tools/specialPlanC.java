package com.nova.tools;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author: wzh
 * @description
 * @date: 2023/07/05 15:17
 */
@Slf4j(topic = "specialPlanC")
public class specialPlanC {

    private static final TimeInterval timer = DateUtil.timer();

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    private static final ForkJoinPool WORK_POOL = new ForkJoinPool(THREAD_COUNT);

    private static final int COUNT = 7_200_000;

    private static final int PAGE_SIZE = 10_000;

    /**
     * coreNum = 10，耗时：1min
     * 手动线程池：三次数据：1177、1182、1176
     * 工作线程池：三次数据：992、991、993
     *
     * @param args
     */
    public static void main(String[] args) {
        String skuId = "100038004347";
        List<String> queryList = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            queryList.add(skuId);
        }
        LongAdder longAdder = new LongAdder();
        Task task = new Task(queryList, queryList.size() < PAGE_SIZE ? queryList.size() : queryList.size() / PAGE_SIZE, longAdder);
        WORK_POOL.invoke(task);
    }

    static class Task extends RecursiveTask<Integer> {

        private final List<String> ids;

        private int threshold = 20;

        private final LongAdder longAdder;

        public Task(List<String> ids, int threshold, LongAdder longAdder) {
            this.ids = ids;
            this.threshold = threshold;
            this.longAdder = longAdder;
        }

        @Override
        protected Integer compute() {
            if (ids.size() > threshold) {
                int mid = ids.size() / 2;
                Task subTask1 = new Task(ids.subList(0, mid), threshold, longAdder);
                Task subTask2 = new Task(ids.subList(mid, ids.size()), threshold, longAdder);
                invokeAll(subTask1, subTask2);
                int subResult1 = subTask1.join();
                int subResult2 = subTask2.join();
                return subResult1 + subResult2;
            } else {
                AtomicInteger sum = new AtomicInteger();
                try {
                    ThreadUtil.sleep(600);
                    longAdder.increment();
                    log.info("time：" + DateUtil.now() + "，耗时：" + timer.interval() + "ms，次数：" + longAdder.longValue());
                } finally {
                    sum.addAndGet(ids.size());
                }
                return sum.get();
            }
        }
    }

}
