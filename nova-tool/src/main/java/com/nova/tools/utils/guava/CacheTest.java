package com.nova.tools.utils.guava;

import com.google.common.cache.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @description: Guava中的缓存实现
 * Guava中的缓存是本地缓存的实现，与ConcurrentMap相似，但不完全一样。最基本的区别就是，
 * ConcurrentMap会一直保存添加进去的元素，除非你主动remove掉。而Guava Cache为了限制内存的使用，通常都会设置自动回收
 * <p>
 * Guava Cache的使用场景：
 * 以空间换取时间，就是你愿意用内存的消耗来换取读取性能的提升
 * 你已经预测到某些数据会被频繁的查询
 * 缓存中存放的数据不会超过内存空间
 * @author: wangzehui
 * @date: 2022/10/13 19:37
 */
public class CacheTest {

    @Test
    public void cacheCreateTest() {
        Cache<String, String> cache = CacheBuilder.newBuilder()
                //设置缓存最大容量
                .maximumSize(100)
                //过期策略，写入一分钟后过期
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
        cache.put("a", "a1");
        String value = cache.getIfPresent("a");
        System.out.println(value);
    }

    @Test
    public void loadingCacheTest() throws ExecutionException {
        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(3)
                //10分钟后刷新缓存的数据
                .refreshAfterWrite(Duration.ofMillis(10))
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        Thread.sleep(1000);
                        System.out.println(key + " load data");
                        return key + " add value";
                    }
                });
        System.out.println(loadingCache.get("a"));
        System.out.println(loadingCache.get("b"));
    }

    @Test
    public void initialCapacityTest() {
        Cache<String, String> cache = CacheBuilder.newBuilder()
                //初始容量
                .initialCapacity(1024)
                .build();
    }


    @Test
    public void maxSizeTest() {
        Cache<String, String> cache = CacheBuilder.newBuilder()
                //缓存最大个数
                .maximumSize(2)
                .build();
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");

        System.out.println(cache.getIfPresent("a"));
        System.out.println(cache.getIfPresent("b"));
        System.out.println(cache.getIfPresent("c"));

        Cache<String, String> cache1 = CacheBuilder.newBuilder()
                //最大容量为1M
                .maximumWeight(1024 * 1024 * 1024)
                //用来计算容量的Weigher
                .weigher(new Weigher<String, String>() {
                    @Override
                    public int weigh(String key, String value) {
                        return key.getBytes().length + value.getBytes().length;
                    }
                })
                .build();
        cache1.put("x", "1");
        cache1.put("y", "2");
        cache1.put("z", "3");

        System.out.println(cache1.getIfPresent("x"));
        System.out.println(cache1.getIfPresent("y"));
        System.out.println(cache1.getIfPresent("z"));

    }

    @Test
    public void expireTest() throws InterruptedException {
        Cache<String, String> cache = CacheBuilder.newBuilder()
                //缓存最大个数
                .maximumSize(100)
                //写入后5分钟过期
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .build();
        cache.put("a", "1");
        int i = 1;
        while (true) {
            System.out.println("第" + i + "秒获取到的数据为：" + cache.getIfPresent("a"));
            i++;
            Thread.sleep(1000);
        }
    }


}
