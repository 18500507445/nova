package com.nova.cache.lock.config;

import com.nova.cache.lock.core.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Redisson自动化配置
 *
 * @author wzh
 * @date 2022/12/26 23:10
 */
@Slf4j
@Configuration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 1)
    public RedissonManager redissonManager(RedissonProperties redissonProperties) {
        RedissonManager redissonManager = new RedissonManager(redissonProperties);
        log.warn("[RedissonManager] 组装完毕，当前连接方式:{}，连接地址:{}", redissonProperties.getType(), redissonProperties.getAddress());
        return redissonManager;
    }

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 2)
    public RedissonLock redissonLock(RedissonManager redissonManager) {
        RedissonLock redissonLock = new RedissonLock();
        redissonLock.setRedissonManager(redissonManager);
        log.warn("[RedissonLock] 组装完毕");
        return redissonLock;
    }
}
