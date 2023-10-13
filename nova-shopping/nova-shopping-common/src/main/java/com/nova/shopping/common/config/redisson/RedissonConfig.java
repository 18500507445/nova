package com.nova.shopping.common.config.redisson;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author: wzh
 * @description: Redisson自动装配
 * @date: 2023/10/13 18:30
 */
@AutoConfiguration
public class RedissonConfig {

    @Bean(name = "redissonLock")
    public RedissonLock redissonLock() {
        return new RedissonLock();
    }
}
