package com.nova.cache;

import cn.hutool.core.util.ObjectUtil;
import com.nova.cache.caffeine.CaffeineCacheUtil;
import com.nova.cache.memcache.MemcacheUtil;
import com.nova.cache.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class CacheApplicationTest {

    @Autowired
    private RedisService redisService;

    @Resource
    private CaffeineCacheUtil caffeineCacheUtil;

    @Resource
    private MemcacheUtil memcacheUtil;

    /**
     * redis测试类
     */
    @Test
    public void redisDemo() {
        String group = "nova-cache:%s";
        String key = String.format(group, "redisDemo");

        redisService.set(key, "1", 5 * 60L);
        Object o = redisService.get(key);
        if (ObjectUtil.isNotNull(o)) {
            System.out.println(o.toString());
        }
    }


    /**
     * caffeineCache测试类
     */
    @Test
    public void caffeineDemo() {
        //application.yml caffeine.cacheNames名称一致
        String cacheName = "caffeine";
        String key = "nova-cache";
        caffeineCacheUtil.putCache(cacheName, key, "1");

        Object cache = caffeineCacheUtil.getCache(cacheName, key);
        System.out.println("cache = " + cache);
    }

    @Test
    public void MemcacheDemo() {
        String key = "nova-cache";
        memcacheUtil.createData(key, "1", 60L);

        Object cache = memcacheUtil.getData(key);

        System.out.println("cache = " + cache);
    }


}
