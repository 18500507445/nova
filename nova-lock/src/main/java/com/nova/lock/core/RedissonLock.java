package com.nova.lock.core;

import com.nova.lock.config.RedissonManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RLock;
import org.redisson.api.RQueue;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁工具类
 * <p>
 * 1）基本用法：lock.lock();
 * 2）支持过期解锁功能,10秒钟以后自动解锁, 无需调用unlock方法手动解锁：lock.lock(10, TimeUnit.SECONDS);
 * 3）尝试加锁，最多等待3秒，上锁以后10秒自动解锁 boolean res = lock.tryLock(3, 10, TimeUnit.SECONDS);
 *
 * @author wzh
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
                log.info("获取Redisson分布式锁[成功]，lockName={}", lockName);
            } else {
                log.info("获取Redisson分布式锁[失败]，lockName={}", lockName);
            }
        } catch (InterruptedException e) {
            log.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
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

    /**
     * 分布式队列，RQueue，FIFO
     *
     * @param name
     * @return
     */
    public boolean rQueueOffer(String name, Object value) {
        RQueue<Object> queue = redissonManager.getRedisson().getQueue(name);
        return queue.offer(value);
    }

    public <t> t rQueuePoll(String name, Class<t> tClass) {
        RQueue<t> queue = redissonManager.getRedisson().getQueue(name);
        return queue.poll();
    }

    public int rQueueSize(String name) {
        return redissonManager.getRedisson().getQueue(name).size();
    }

    /**
     * 分布式队列，BQueue，阻塞
     */
    public boolean bQueueOffer(String name, Object value, long time, TimeUnit unit) {
        try {
            RBlockingQueue<Object> blockingQueue = redissonManager.getRedisson().getBlockingQueue(name);
            return blockingQueue.offer(value, time, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public <t> t bQueuePoll(String name, long time, TimeUnit unit, Class<t> tClass) {
        try {
            RBlockingQueue<t> blockingQueue = redissonManager.getRedisson().getBlockingQueue(name);
            return blockingQueue.poll(time, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int bQueueSize(String name) {
        return redissonManager.getRedisson().getBlockingQueue(name).size();
    }


}
