package com.nova.limit.bucket;

/**
 * @description: 令牌桶抽象能力类
 * @author: wzh
 * @date: 2023/4/18 20:44
 */
public abstract class AbstractTokenBucket implements TokenBucket {

    /**
     * 通过令牌桶key获取令牌
     *
     * @param key 令牌桶key
     * @return 是否成功获取
     * @throws Exception 令牌桶不存在抛出异常
     */
    public boolean getToken(String key) throws Exception {
        return decreaseCapacity(key);
    }

    public abstract void createTokenBucket(Bucket bucket);

    /**
     * 通过key获取令牌桶
     *
     * @param key key
     * @return 令牌桶
     * @throws Exception 如果令牌桶不存在抛出异常
     */
    public abstract Bucket getTokenBucket(String key) throws Exception;

    public abstract boolean decreaseCapacity(String key) throws Exception;

    public Bucket getNowCapacity(String key) throws Exception {
        Bucket bucket = getTokenBucket(key);
        long now = System.currentTimeMillis();
        // 这段时间桶子流入的速度
        int putCapacity = (int) ((now - bucket.getStartTime()) / 1000) * bucket.getPutSpeed();
        // 获取当前容量
        int nowCapacity = Math.min(bucket.getCapacity() + putCapacity, bucket.getMaxCapacity());
        // 如果容量不够直接返回
        if (nowCapacity < 1) {
            return null;
        }
        bucket.setCapacity(nowCapacity - 1);
        bucket.setStartTime(System.currentTimeMillis());
        return bucket;
    }


}
