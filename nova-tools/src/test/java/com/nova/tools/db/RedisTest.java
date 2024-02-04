package com.nova.tools.db;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.ConsistentHash;
import com.google.common.collect.Lists;
import com.nova.starter.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * @description: redis-starter测试类
 * @author: wzh
 * @date: 2023/4/22 21:08
 */
@SpringBootTest
@Slf4j(topic = "testRedis")
public class RedisTest {

    @Autowired
    private RedisService redisService;


    @Autowired
    private RedisTemplate<String, Object> secondRedisTemplate;

    @Test
    public void testRedis() {
        final Object o = redisService.getHashValue("testHash", "1");
        final Object map = redisService.getHash("testHash");
        System.err.println("o = " + o);
        System.err.println("map = " + map);
    }

    /**
     * 第二个redis数据源
     */
    @Test
    public void secondRedis() {
        final Object o1 = redisService.getHashValue("testHash", "1");
        System.err.println("o1 = " + o1);

        secondRedisTemplate.opsForValue().set("database2", "1");
        Object o = secondRedisTemplate.opsForValue().get("database2");
        System.err.println("o = " + o);
    }


    /**
     * 400w 数据 hash 230m内存
     */
    @Test
    public void calculateRedisHash() {
        int num = 4000000;
        int sharding = 80000;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(i);
        }
        TimeInterval timer = DateUtil.timer();
        List<List<Integer>> partition = Lists.partition(list, sharding);
        for (List<Integer> integers : partition) {
            Map<String, Object> hashMap = new HashMap<>(sharding);
            for (Integer integer : integers) {
                hashMap.put(String.valueOf(integer), integer);
            }
            redisService.setHash("testHash", hashMap);
            long interval = timer.interval();
            System.err.println("耗时 ：" + interval + " ms");
            timer.restart();
        }
        System.err.println("耗时：" + timer.interval());
    }

    @Test
    public void demoA() {
        String key = "testHash";
        long total = 4000000;
        final long sharding = 10000L;
        redisService.scanHashAndDel(key, total, sharding);
    }

    /**
     * 400w数据 list 30m内存
     */
    @Test
    public void calculateRedisList() {
        int num = 4000000;
        int sharding = 80000;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(i);
        }
        TimeInterval timer = DateUtil.timer();
        List<List<Integer>> partition = Lists.partition(list, sharding);
        for (List<Integer> integers : partition) {
            redisService.setList("testList", integers);
        }
        System.err.println("耗时：" + timer.interval());
    }


    private static final int NUM_KEYS = 16;

    private static final String[] KEYS = {
            "key0", "key1", "key2", "key3", "key4", "key5", "key6", "key7",
            "key8", "key9", "key10", "key11", "key12", "key13", "key14", "key15"
    };

    // 构建一致性哈希环
    public static final ConsistentHash<String> CONSISTENT_HASH = new ConsistentHash<>(NUM_KEYS, Arrays.asList(KEYS));

    @Test
    public void hashCodeTest() {
        String product = "12334";
        String key = CONSISTENT_HASH.get(product);
        System.out.println("key = " + key);
    }

    @Test
    public void testLock() {
        String key = "testLock";
        boolean lock = redisService.lock(key, key, 1000);
        System.err.println("lock = " + lock);


        Object o = redisService.get(key);
        System.err.println("o = " + o);

        boolean unlock = redisService.unlock(key, key);
        System.err.println("unlock = " + unlock);
    }


}
