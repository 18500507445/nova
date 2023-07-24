package com.nova.tools;

import com.starter.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @description: redis-starter测试类
 * @author: wzh
 * @date: 2023/4/22 21:08
 */
@SpringBootTest
@Slf4j(topic = "testRedis")
public class TestRedis {

    @Resource
    private RedisService redisService;

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testRedis() {
        final Object o = redisService.get("234");
        System.out.println("o = " + o);
    }

    /**
     * 测试redisson加锁、解锁
     */
    @Test
    public void testRedisson() {
        String key = "redissonLock";

        boolean lock = lock(key, 100L);
        System.out.println("lock = " + lock);

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
        redissonClient.getLock(key).unlock();
    }


}
