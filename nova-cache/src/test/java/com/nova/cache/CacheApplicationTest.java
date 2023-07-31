package com.nova.cache;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.nova.cache.caffeine.CaffeineCacheUtil;
import com.nova.cache.memcached.MemcachedUtil;
import com.nova.cache.redis.RedisService;
import com.nova.common.utils.thread.Threads;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CacheApplicationTest {

    @Resource
    private RedisService redisService;

    @Resource
    private CaffeineCacheUtil caffeineCacheUtil;

    @Resource
    private MemcachedUtil memcachedUtil;

    /**
     * redis 设置分组
     */
    @Test
    public void setGroup() {
        String groupName = "nova-cache:%s";
        String key = String.format(groupName, "redisDemo");
        redisService.set(key, "1", 60L);
        Object o = redisService.get(key);
        if (ObjectUtil.isNotNull(o)) {
            System.err.println(o);
        }
    }

    /**
     * 存储Object对象或者json字符串
     * 应用场景：存储简单的字符，比如存个标志位，或者分布式锁，或者当前优惠券的剩余数量。
     */
    @Test
    public void setObjectOrStr() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("1", "a");
        jsonObject.put("2", "b");
        redisService.set("setJson", jsonObject, 60L);
        redisService.set("setStr", jsonObject.toString(), 60L);
    }

    /**
     * 计数，默认过期-1永久
     */
    @Test
    public void incr() {
        Threads.sleep(2000);
        String key = "total";
        redisService.incr(key, 5L);
        Threads.sleep(5000);
        redisService.decr(key, 1L);
        redisService.expire(key, 60L);

        Threads.sleep(5000);
        redisService.expire(key, 60L);
    }

    /**
     * 放入LinkedHashMap
     */
    @Test
    public void setMap() {
        String key = "setHashMap";
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("1", "a");
        map.put("2", "b");
        map.put("3", "c");
        redisService.setHash(key, map);

        redisService.delHash(key, "4");
    }

    /**
     * 字符串列表，底层双向链表实现，值可重复
     * 应用场景：关注列表、粉丝列表等
     */
    @Test
    public void setList() {
        String key = "setList";
        redisService.setList(key, Arrays.asList("1", "2", "3"));

        List<Object> objects = redisService.getList(key, 0L, -1L);

        System.err.println("objects = " + objects);
    }

    /**
     * 内部实现是一个value永远为null的HashMap，是通过计算hash的方式来快速排重的，这也是set能提供判断一个成员是否在集合内的原因。
     * 应用场景：黑名单，白名单之类
     */
    @Test
    public void setSet() {
        redisService.setSet("setSet", "1", "2", "3", "3");
    }

    /**
     * 内部使用HashMap和跳跃表(SkipList)来保证数据的存储和有序
     * 应用场景：排行榜
     */
    @Test
    public void setZSet() {
        String key = "rank:2023-04-15";
        redisService.setZSet(key, "A", 33);
        redisService.setZSet(key, "B", 44);
        redisService.setZSet(key, "C", 55);

        final JSONArray zSet = redisService.getZSet(key, 0L, -1L);
        System.err.println(zSet);
    }

    /**
     * 支持合并，单合unionZSet、多合unionZSetList
     */
    @Test
    public void unionZSet() {
        List<String> keys = Arrays.asList("rank:2023-04-14", "rank:2023-04-15");
        redisService.unionZSetList("rank:2023-04-13", keys, "unionZSet");
    }

    @Test
    public void del() {
        redisService.del("unionZSet");
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
        System.err.println("cache = " + cache);
    }

    /**
     * Memcached测试
     */
    @Test
    public void MemcachedDemo() {
        String key = "nova-cache";
        memcachedUtil.createData(key, "1", 60L);

        Object cache = memcachedUtil.getData(key);

        System.err.println("cache = " + cache);
    }


}
