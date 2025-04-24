package com.nova.cache.lock.config;

import lombok.AllArgsConstructor;
import org.redisson.config.Config;

/**
 * RedissonConfigContext
 * Redisson配置上下文，产出真正的Redisson的Config
 *
 * @author: wzh
 * @date: 2022/12/26 23:10
 */
@AllArgsConstructor
public class RedissonConfigContext {

    private final RedissonConfigStrategy redissonConfigStrategy;

    /**
     * 上下文根据构造中传入的具体策略产出真实的Redisson的Config
     *
     * @param redissonProperties redisson配置
     * @return Config
     */
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        return this.redissonConfigStrategy.createRedissonConfig(redissonProperties);
    }
}
