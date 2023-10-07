package com.nova.cache.redis.caffeine;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: Caffeine本地缓存配置
 * @author: wzh
 * @date: 2022/12/20 15:40
 */
@Data
@Component
@ConfigurationProperties(prefix = "caffeine", ignoreInvalidFields = true)
public class CaffeineConfig {

    /**
     * cache name配置
     */
    private String cacheNames;

    /**
     * 初始的缓存空间大小
     */
    private String initCapacity;

    /**
     * 缓存最大的条数
     */
    private String maxSize;

    /**
     * 最后一次写入或访问后经过固定的时间
     */
    private String expireAfterAccess;

    /**
     * 创建缓存或最新一次更新缓存后经过固定的时间间隔，刷新缓存
     */
    private String refreshAfterWrite;
}
