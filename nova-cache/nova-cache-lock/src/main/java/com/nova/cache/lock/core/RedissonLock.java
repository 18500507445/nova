package com.nova.cache.lock.core;

import com.nova.cache.lock.config.RedissonManager;
import com.nova.cache.lock.enums.LockType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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
    public boolean lock(LockType lockType, String lockName, long expireSeconds) {
        RLock rLock;
        switch (lockType) {
            case FAIR:
                rLock = redissonManager.getRedisson().getFairLock(lockName);
                break;
            case READ:
                RReadWriteLock readWriteLock = redissonManager.getRedisson().getReadWriteLock(lockName);
                rLock = readWriteLock.readLock();
                break;
            case WRITE:
                RReadWriteLock rwLock = redissonManager.getRedisson().getReadWriteLock(lockName);
                rLock = rwLock.writeLock();
                break;
            default:
                //默认可重入锁
                rLock = redissonManager.getRedisson().getLock(lockName);
        }
        boolean flag;
        try {
            flag = rLock.tryLock(0, expireSeconds, TimeUnit.SECONDS);
            if (flag) {
                log.info("获取Redisson分布式锁[成功]，lockName={}", lockName);
            } else {
                log.info("获取Redisson分布式锁[失败]，lockName={}", lockName);
            }
        } catch (InterruptedException e) {
            log.error("获取Redisson分布式锁[异常]，lockName={}", lockName, e);
            return false;
        }
        return flag;
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
     * 加锁
     */
    public void isLock(String lockName) {
        RLock rLock = redissonManager.getRedisson().getLock(lockName);
        try {
            rLock.lock();
        } catch (Exception e) {
            log.error("获取Redisson分布式锁[异常]，lockName={}", lockName, e);
        }
    }

    /**
     * 查询是否有锁
     */
    public boolean isLocked(String lockName) {
        return redissonManager.getRedisson().getLock(lockName).isLocked();
    }

    /**
     * 分布式队列，RQueue，FIFO
     *
     * @param name
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

    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     * @param consumer   自定义处理
     */
    public <T> void publish(String channelKey, T msg, Consumer<T> consumer) {
        RTopic topic = redissonManager.getRedisson().getTopic(channelKey);
        topic.publish(msg);
        consumer.accept(msg);
    }

    /**
     * 订阅通道接收消息
     *
     * @param channelKey 通道key
     * @param clazz      消息类型
     * @param consumer   自定义处理
     */
    public <T> void subscribe(String channelKey, Class<T> clazz, Consumer<T> consumer) {
        RTopic topic = redissonManager.getRedisson().getTopic(channelKey);
        topic.addListener(clazz, (channel, msg) -> consumer.accept(msg));
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间
     */
    public <T> void setCacheObject(final String key, final T value, final Duration duration) {
        RBatch batch = redissonManager.getRedisson().createBatch();
        RBucketAsync<T> bucket = batch.getBucket(key);
        bucket.setAsync(value);
        bucket.expireAsync(duration);
        batch.execute();
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        RBucket<T> rBucket = redissonManager.getRedisson().getBucket(key);
        return rBucket.get();
    }

    /**
     * 检查缓存对象是否存在
     *
     * @param key 缓存的键值
     */
    public boolean isExistsObject(final String key) {
        return redissonManager.getRedisson().getBucket(key).isExists();
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> boolean setCacheList(final String key, final List<T> dataList) {
        RList<T> rList = redissonManager.getRedisson().getList(key);
        return rList.addAll(dataList);
    }


    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        RList<T> rList = redissonManager.getRedisson().getList(key);
        return rList.readAll();
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存的键值
     */
    public boolean deleteObject(final String key) {
        return redissonManager.getRedisson().getBucket(key).delete();
    }

    /**
     * 设置原子值
     *
     * @param key   Redis键
     * @param value 值
     */
    public void setAtomicValue(String key, long value) {
        RAtomicLong atomic = redissonManager.getRedisson().getAtomicLong(key);
        atomic.set(value);
    }

    /**
     * 获取原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public long getAtomicValue(String key) {
        RAtomicLong atomic = redissonManager.getRedisson().getAtomicLong(key);
        return atomic.get();
    }

    /**
     * 递增原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public long incrAtomicValue(String key) {
        RAtomicLong atomic = redissonManager.getRedisson().getAtomicLong(key);
        return atomic.incrementAndGet();
    }

    /**
     * 递减原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public long decrAtomicValue(String key) {
        RAtomicLong atomic = redissonManager.getRedisson().getAtomicLong(key);
        return atomic.decrementAndGet();
    }

    /**
     * 检查redis中是否存在key
     *
     * @param key 键
     */
    public Boolean hasKey(String key) {
        RKeys rKeys = redissonManager.getRedisson().getKeys();
        return rKeys.countExists(key) > 0;
    }
}
