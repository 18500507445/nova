package com.nova.common.utils.id;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * ID生成器工具类
 */
public class IdUtils {

    private IdUtils() {

    }


    /**
     /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUuid() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUuid() {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUuid() {
        return UUID.fastUUID().toString(true);
    }

    /**
     * 生成雪花id
     *
     * @param workerId     机器id    0-31
     * @param datacenterId 数据中心id  0-31
     * @return
     */
    public static Long snowId(long workerId, long datacenterId) {
        final Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

    /**
     * 测试多线程下，生成雪花id
     */
    public static void main(String[] args) {
        TimeInterval timer = DateUtil.timer();
        final Set<Long> set = new ConcurrentHashSet<>();
        int threadCount = 100;
        final int idCountPerThread = 10000;
        final CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            ThreadUtil.execute(() -> {
                for (int i1 = 0; i1 < idCountPerThread; i1++) {
                    long id = snowId(1, 2);
                    set.add(id);
                    Console.log("Add new id: {}", id);
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new UtilException(e);
        }
        System.out.println("耗时：" + timer.interval() + "ms");
        Assert.equals(threadCount * idCountPerThread, set.size());
    }
}
