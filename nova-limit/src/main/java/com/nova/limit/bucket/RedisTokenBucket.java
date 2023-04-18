package com.nova.limit.bucket;

import com.alibaba.fastjson2.JSONObject;
import com.nova.common.utils.thread.Threads;
import com.nova.limit.utils.JedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/18 20:52
 */
@Component
public class RedisTokenBucket extends AbstractTokenBucket {

    @Resource
    private JedisUtil jedisUtil;

    private final String hashKey = "RedisTokenBucket";

    public boolean decreaseCapacity(String key) {
        boolean lock = lock(hashKey + key);
        try {
            if (!lock) {
                return false;
            }
            Bucket bucket = this.getNowCapacity(key);
            if (bucket == null) {
                return false;
            }
            return jedisUtil.hashSet(hashKey, bucket.getKey(), JSONObject.toJSONString(bucket));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (lock) {
                unlock(hashKey + key);
            }
        }
    }

    @Override
    public void createTokenBucket(Bucket bucket) {
        String tokenBucketStr = jedisUtil.hashGet(hashKey, bucket.getKey());
        if (StringUtils.isBlank(tokenBucketStr)) {
            jedisUtil.hashSet(hashKey, bucket.getKey(), JSONObject.toJSONString(bucket));
            return;
        }
        // 变更配置
        Bucket redisBucket = JSONObject.parseObject(tokenBucketStr, Bucket.class);
        if (redisBucket.getMaxCapacity() != bucket.getMaxCapacity()
                || redisBucket.getPutSpeed() != bucket.getPutSpeed()) {
            redisBucket.setMaxCapacity(bucket.getMaxCapacity());
            redisBucket.setPutSpeed(bucket.getPutSpeed());
            boolean lock = lock(hashKey + bucket.getKey());
            try {
                if (lock) {
                    jedisUtil.hashSet(hashKey, bucket.getKey(), JSONObject.toJSONString(redisBucket));
                }
            } finally {
                if (lock) {
                    unlock(hashKey + bucket.getKey());
                }
            }

        }
    }


    @Override
    public Bucket getTokenBucket(String key) throws Exception {
        String tokenBucket = jedisUtil.hashGet(hashKey, key);
        if (StringUtils.isBlank(tokenBucket)) {
            throw new Exception("not find bucket config,key:" + key);
        }
        return JSONObject.parseObject(tokenBucket, Bucket.class);
    }


    /**
     * 加锁
     *
     * @param key 锁名称
     * @return 加锁成功返回true，加锁失败返回false，未拿到锁
     */
    private boolean lock(String key) {
        int timeOut = 5000;
        try {
            long start = System.currentTimeMillis();
            for (; ; ) {
                if (jedisUtil.setNx(key, String.valueOf(System.currentTimeMillis()), 5)) {
                    return true;
                }
                if ((start + timeOut) > System.currentTimeMillis()) {
                    System.out.println("超时未拿到执行锁");
                    //超时
                    return false;
                }
                Threads.sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param key 锁名称
     */
    private void unlock(String key) {
        jedisUtil.del(key);
    }
}
