package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * {@link IdUtil} Id工具类单元测试
 *
 * @author: looly
 */
public class IdUtilTest {

    @Test
    public void randomUUIDTest() {
        String simpleUUID = IdUtil.simpleUUID();
        Assert.equals(32, simpleUUID.length());

        String randomUUID = IdUtil.randomUUID();
        Assert.equals(36, randomUUID.length());
    }

    @Test
    public void fastUUIDTest() {
        String simpleUUID = IdUtil.fastSimpleUUID();
        Assert.equals(32, simpleUUID.length());

        String randomUUID = IdUtil.fastUUID();
        Assert.equals(36, randomUUID.length());
    }

    /**
     * UUID的性能测试
     */
    @Test
    public void benchTest() {
        TimeInterval timer = DateUtil.timer();
        for (int i = 0; i < 1000000; i++) {
            IdUtil.simpleUUID();
        }
        Console.log(timer.interval());

        timer.restart();
        for (int i = 0; i < 1000000; i++) {
            //noinspection ResultOfMethodCallIgnored
            UUID.randomUUID().toString().replace("-", "");
        }
        Console.log(timer.interval());
    }

    @Test
    public void objectIdTest() {
        String id = IdUtil.objectId();
        Assert.equals(24, id.length());
    }

    @Test
    public void getSnowflakeTest() {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();
        Assert.isTrue(id > 0);
    }

    @Test
    public void snowflakeBenchTest() {
        final Set<Long> set = new ConcurrentHashSet<>();
        final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

        //线程数
        int threadCount = 100;
        //每个线程生成的ID数
        final int idCountPerThread = 10000;
        final CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            ThreadUtil.execute(() -> {
                for (int i1 = 0; i1 < idCountPerThread; i1++) {
                    long id = snowflake.nextId();
                    set.add(id);
//						Console.log("Add new id: {}", id);
                }
                latch.countDown();
            });
        }

        //等待全部线程结束
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new UtilException(e);
        }
        Assert.equals(threadCount * idCountPerThread, set.size());
    }

    @Test
    public void snowflakeBenchTest2() {
        final Set<Long> set = new ConcurrentHashSet<>();

        //线程数
        int threadCount = 100;
        //每个线程生成的ID数
        final int idCountPerThread = 10000;
        final CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            ThreadUtil.execute(() -> {
                for (int i1 = 0; i1 < idCountPerThread; i1++) {
                    long id = IdUtil.getSnowflake(1, 1).nextId();
                    set.add(id);
//						Console.log("Add new id: {}", id);
                }
                latch.countDown();
            });
        }

        //等待全部线程结束
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new UtilException(e);
        }
        Assert.equals(threadCount * idCountPerThread, set.size());
    }

    @Test
    public void getDataCenterIdTest() {
        //按照mac地址算法拼接的算法，maxDatacenterId应该是0xffffffffL>>6-1此处暂时按照0x7fffffffffffffffL-1，防止最后取模溢出
        final long dataCenterId = IdUtil.getDataCenterId(Long.MAX_VALUE);
        Assert.isTrue(dataCenterId >= 0);
    }
}
