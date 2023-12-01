package com.nova.tools;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.google.common.collect.Lists;
import com.nova.starter.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate<String, Object> secondRedisTemplate;

    /**
     * 测试redisson加锁、解锁
     */
    @Test
    public void testRedisson() {
        String key = "redissonLock";

        boolean lock = lock(key, 100L);
        System.err.println("lock = " + lock);

        release(key);
    }

    public boolean lock(String key, long expireSeconds) {
        RLock rLock = redissonClient.getLock(key);
        boolean getLock;
        try {
            getLock = rLock.tryLock(0, expireSeconds, TimeUnit.SECONDS);
            if (getLock) {
                log.info("获取Redisson分布式锁[成功]，key={}", key);
            } else {
                log.info("获取Redisson分布式锁[失败]，key={}", key);
            }
        } catch (InterruptedException e) {
            log.error("获取Redisson分布式锁[异常]，key=" + key, e);
            return false;
        }
        return getLock;
    }

    public void release(String key) {
        log.info("获取Redisson分布式锁[解锁]，key={}", key);
        redissonClient.getLock(key).unlock();
    }

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


}
