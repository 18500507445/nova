package com.nova.tools.utils.hutool.core.lang;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Snowflake单元测试（雪花算法）
 *
 * @author: Looly
 */
public class SnowflakeTest {

    @Test
    public void snowflakeTest1() {
        //构建Snowflake，提供终端ID和数据中心ID
        Snowflake idWorker = new Snowflake(0, 0);
        long nextId = idWorker.nextId();
        Assert.isTrue(nextId > 0);
    }

    @Test
    public void snowflakeTest() {
        HashSet<Long> hashSet = new HashSet<>();

        //构建Snowflake，提供终端ID和数据中心ID
        Snowflake idWorker = new Snowflake(0, 0);
        for (int i = 0; i < 1000; i++) {
            long id = idWorker.nextId();
            hashSet.add(id);
        }
        Assert.equals(1000L, hashSet.size());
    }

    @Test
    public void snowflakeGetTest() {
        //构建Snowflake，提供终端ID和数据中心ID
        Snowflake idWorker = new Snowflake(1, 2);
        long nextId = idWorker.nextId();

        Assert.equals(1, idWorker.getWorkerId(nextId));
        Assert.equals(2, idWorker.getDataCenterId(nextId));
        Assert.isTrue(idWorker.getGenerateDateTime(nextId) - System.currentTimeMillis() < 10);
    }

    @Test
    public void uniqueTest() {
        // 测试并发环境下生成ID是否重复
        Snowflake snowflake = IdUtil.getSnowflake(0, 0);

        Set<Long> ids = new ConcurrentHashSet<>();
        ThreadUtil.concurrencyTest(100, () -> {
            for (int i = 0; i < 50000; i++) {
                if (false == ids.add(snowflake.nextId())) {
                    throw new UtilException("重复ID！");
                }
            }
        });
    }

    @Test
    public void getSnowflakeLengthTest() {
        for (int i = 0; i < 1000; i++) {
            final long l = IdUtil.getSnowflake(0, 0).nextId();
            Assert.equals(19, StrUtil.toString(l).length());
        }
    }

    @Test
    public void snowflakeRandomSequenceTest() {
        final Snowflake snowflake = new Snowflake(null, 0, 0,
                false, Snowflake.DEFAULT_TIME_OFFSET, 2);
        for (int i = 0; i < 1000; i++) {
            final long id = snowflake.nextId();
            Console.log(id);
            ThreadUtil.sleep(10);
        }
    }

    @Test
    public void uniqueOfRandomSequenceTest() {
        // 测试并发环境下生成ID是否重复
        final Snowflake snowflake = new Snowflake(null, 0, 0,
                false, Snowflake.DEFAULT_TIME_OFFSET, 100);

        Set<Long> ids = new ConcurrentHashSet<>();
        ThreadUtil.concurrencyTest(100, () -> {
            for (int i = 0; i < 50000; i++) {
                if (false == ids.add(snowflake.nextId())) {
                    throw new UtilException("重复ID！");
                }
            }
        });
    }
}
