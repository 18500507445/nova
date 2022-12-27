package com.nova.lock.config.strategy;

import com.nova.lock.config.RedissonProperties;
import org.redisson.config.Config;

/**
 * RedissonConfigStrategy
 * Redisson配置构建接口
 *
 * @author wangzehui
 * @date 2022/12/26 23:10
 */
public interface RedissonConfigStrategy {

    /**
     * 根据不同的Redis配置策略创建对应的Config
     *
     * @param redissonProperties redisson配置
     * @return Config
     */
    Config createRedissonConfig(RedissonProperties redissonProperties);
}
