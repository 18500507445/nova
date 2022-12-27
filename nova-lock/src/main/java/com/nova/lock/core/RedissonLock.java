package com.nova.lock.core;

import com.nova.lock.config.RedissonManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁工具类
 * <p>
 * 1）基本用法：lock.lock();
 * 2）支持过期解锁功能,10秒钟以后自动解锁, 无需调用unlock方法手动解锁：lock.lock(10, TimeUnit.SECONDS);
 * 3）尝试加锁，最多等待3秒，上锁以后10秒自动解锁 boolean res = lock.tryLock(3, 10, TimeUnit.SECONDS);
 *
 * @author wangzehui
 * @date 2022/12/26 20:53
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RedissonLock {

    private RedissonManager redissonManager;

    /**
     * 加锁操作
     *
     * @return boolean
     */
    public boolean lock(String lockName, long expireSeconds) {
        RLock rLock = redissonManager.getRedisson().getLock(lockName);
        boolean getLock;
        try {
            getLock = rLock.tryLock(0, expireSeconds, TimeUnit.SECONDS);
            if (getLock) {
                log.info("获取Redisson分布式锁[成功],lockName={}", lockName);
            } else {
                log.info("获取Redisson分布式锁[失败],lockName={}", lockName);
            }
        } catch (InterruptedException e) {
            log.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
            e.printStackTrace();
            return false;
        }
        return getLock;
    }

    /**
     * 解锁
     *
     * @param lockName 锁名称
     */
    public void release(String lockName) {
        redissonManager.getRedisson().getLock(lockName).unlock();
    }

    /**
     * 加锁操作
     *
     * @return boolean
     */
    public void isLock(String lockName) {
        RLock rLock = redissonManager.getRedisson().getLock(lockName);
        try {
            rLock.lock();
        } catch (Exception e) {
            log.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
        }
    }

    /**
     * 查询是否有锁
     */
    public boolean isLocked(String lockName) {
        RLock rLock = redissonManager.getRedisson().getLock(lockName);
        return rLock.isLocked();
    }
}
