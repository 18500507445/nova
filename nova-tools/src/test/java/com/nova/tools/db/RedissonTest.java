package com.nova.tools.db;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RFuture;
import org.redisson.api.RKeys;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: wzh
 * @description: 分布式锁
 * @date: 2024/01/12 10:25
 */
@SpringBootTest
@Slf4j(topic = "RedissonTest")
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;

    public boolean lock(String key, long expireSeconds) {
        RLock rLock = redissonClient.getLock(key);
        boolean flag = false;
        try {
            flag = rLock.tryLock(0, expireSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Redisson分布式锁【异常】，key = " + key, e);
        } finally {
            if (flag) {
                log.info("Redisson分布式锁【成功】，key = {}", key);
            } else {
                log.info("Redisson分布式锁【失败】，key = {}", key);
            }
        }
        return flag;
    }

    public boolean asyncLock(String key, long expireSeconds) {
        RLock rLock = redissonClient.getLock(key);
        boolean flag = false;
        try {
            flag = rLock.tryLockAsync(0, expireSeconds, TimeUnit.SECONDS).get();
        } catch (Exception e) {
            log.error("Redisson分布式锁【异步加锁异常】，key = " + key, e);
        } finally {
            if (flag) {
                log.info("Redisson分布式锁【异步加锁成功】，key = {}", key);
            } else {
                log.info("Redisson分布式锁【异步加锁失败】，key = {}", key);
            }
        }
        return flag;
    }

    public void release(String key) {
        log.info("Redisson分布式锁【解锁】，key = {}", key);
        redissonClient.getLock(key).unlock();
    }

    public void asyncRelease(String key, Long... threadId) {
        log.info("Redisson分布式锁【异步解锁】，key = {}", key);
        if (ArrayUtil.isNotEmpty(threadId)) {
            redissonClient.getLock(key).unlockAsync(threadId[0]);
        } else {
            redissonClient.getLock(key).unlockAsync();
        }
    }

    /**
     * 查询是否有锁
     */
    public boolean isLocked(String lockName) {
        return redissonClient.getLock(lockName).isLocked();
    }

    /**
     * 查询是否有异步锁
     */
    public boolean isAsyncLocked(String lockName) {
        Boolean isLocked = Boolean.FALSE;
        RFuture<Boolean> lockedAsync = redissonClient.getLock(lockName).isLockedAsync();
        try {
            isLocked = lockedAsync.get();
        } catch (InterruptedException | ExecutionException e) {
            log.info("Redisson分布式锁【是否有异步锁失败】", e);
        }
        return isLocked;
    }

    /**
     * 获取过期时间
     */
    public Long getExpirationTime(String key) {
        RKeys rKeys = redissonClient.getKeys();
        long milliseconds = rKeys.remainTimeToLive(key);
        if (milliseconds >= 0) {
            return milliseconds / 1000;
        } else {
            return milliseconds;
        }
    }

    /**
     * 测试redisson加锁、解锁
     */
    @Test
    public void testLock() {
        String key = "redissonLock";
        try {
            release(key);
        } catch (Exception e) {
            log.info("异常",e);
        }
        boolean lock = lock(key, 100L);
        boolean isLock = isLocked(key);
        System.err.println("lock = " + lock);
        System.err.println("isLock = " + isLock);

        release(key);
        System.out.println("isLocked(key) = " + isLocked(key));
        System.out.println("getExpirationTime(key) = " + getExpirationTime(key));
        boolean lock2 = lock(key, 100L);

        System.out.println("getExpirationTime(key) = " + getExpirationTime(key));
        System.err.println("lock2 = " + lock2);

        System.out.println("isLocked(key) = " + isLocked(key));
    }


    /**
     * 测试异步加锁，解锁
     */
    @Test
    public void testAsyncLock() {
        String key = "asyncRedissonLock";
        boolean lock = asyncLock(key, 1000L);
        System.err.println("lock = " + lock + "，主线程id：" + Thread.currentThread().getId());

        boolean asyncLocked = isAsyncLocked(key);
        System.err.println("asyncLocked = " + asyncLocked);

        long threadId = Thread.currentThread().getId();

        ThreadUtil.sleep(5000);
        new Thread(() -> {
            asyncRelease(key, threadId);
            System.err.println("子线程id：" + Thread.currentThread().getId());
        }).start();

    }


}
