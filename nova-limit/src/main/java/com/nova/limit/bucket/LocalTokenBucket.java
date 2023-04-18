package com.nova.limit.bucket;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 本地令牌桶
 * @author: wzh
 * @date: 2023/4/18 20:49
 */
@Component
public class LocalTokenBucket extends AbstractTokenBucket {

    /**
     * 本地令牌桶存储Map
     * key - 令牌key
     * value - 令牌容量
     */
    private final Map<String, Bucket> BUCKET_MAP = new ConcurrentHashMap<>(8);

    @Override
    public void createTokenBucket(Bucket bucket) {
        if (!BUCKET_MAP.containsKey(bucket.getKey())) {
            BUCKET_MAP.put(bucket.getKey(), bucket);
        }
    }

    @Override
    public Bucket getTokenBucket(String key) throws Exception {
        Bucket bucket = BUCKET_MAP.get(key);
        if (bucket == null) {
            throw new Exception("not find bucket config,key:" + key);
        }
        return bucket;
    }

    @Override
    public boolean decreaseCapacity(String key) throws Exception {
        Bucket tokenBucket = getTokenBucket(key);
        synchronized (tokenBucket) {
            Bucket bucket = this.getNowCapacity(key);
            return bucket != null;
        }
    }
}
