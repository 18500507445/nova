package com.nova.cache.redis.caffeine;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @description: Caffeine manager配置
 * @author: wzh
 * @date: 2022/12/20 15:40
 */
@Component
public class CaffeineConfig {

    @Resource
    private CaffeineProperties caffeineProperties;

    @Bean(name = "caffeine")
    public CacheManager initCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .initialCapacity(Convert.toInt(caffeineProperties.getInitCapacity(), 100))
                .maximumSize(Convert.toInt(caffeineProperties.getMaxSize(), 1000))
                .expireAfterAccess(Convert.toInt(caffeineProperties.getExpireAfterAccess(), 1000), TimeUnit.SECONDS);
        caffeineCacheManager.setCaffeine(caffeine);
        caffeineCacheManager.setCacheNames(StrUtil.isEmpty(caffeineProperties.getCacheNames()) ?
                Lists.newArrayList("caffeine") : Arrays.asList(caffeineProperties.getCacheNames().split(";")));
        caffeineCacheManager.setAllowNullValues(false);
        return caffeineCacheManager;
    }

    @Bean
    public CacheLoader<Object, Object> cacheLoader() {
        return new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object o) throws Exception {
                return null;
            }

            @Override
            public Object reload(Object key, Object oldValue) {
                // 重写这个方法将oldValue值返回，然后刷新缓存
                return oldValue;
            }
        };
    }
}
