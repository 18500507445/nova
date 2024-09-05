package com.nova.cache.redis;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.nova.cache.redis.caffeine.CacheUtil;
import com.nova.cache.redis.memcached.MemcachedUtil;
import com.nova.cache.redis.redis.RedisService;
import com.nova.common.utils.id.IdUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class RedisApplicationTest {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private MemcachedUtil memcachedUtil;

    //分组
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
        ThreadUtil.sleep(2000);
        String key = "total";
        redisService.incr(key, 5L);
        ThreadUtil.sleep(5000);
        redisService.decr(key, 1L);
        redisService.expire(key, 60L);

        ThreadUtil.sleep(5000);
        if (!redisService.hasKey(key)) {
            redisService.expire(key, 60L);
        }
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
        map.put("4", "d");
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
        Set<Object> set = new HashSet<>();
        set.add("1");
        set.add("2");
        set.add("3");
        redisService.setSet("setSet", set);
    }

    /**
     * 内部使用HashMap和跳跃表(SkipList)来保证数据的存储和有序
     * 应用场景：排行榜
     */
    @Test
    public void setZSet() {
        String key = "rank:2023-04-13";
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
        //将13、14、15合并到total
        redisService.unionZSetList("rank:2023-04-13", keys, "rank:total");
    }

    @Test
    public void testLock() {
        for (int i = 0; i < 50; i++) {
            String uuid = IdUtils.randomUuid();
            boolean lock = redisService.lock(uuid, 1);
            System.err.println("lock = " + lock + ", " + i);
            redisService.unlock(uuid);
        }
    }


    /**
     * BitMap
     * 应用场景如下：
     * （1）点赞，key存业务id，value存用户id
     * （2）日活，key存日期，value存用户id
     * （3）用户在线状态
     */
    @Test
    public void testBitMap() {
        String key = "bitmap";
        for (int userId = 1; userId <= 10; userId++) {
            redisService.setBit(key, userId, RandomUtil.randomBoolean());
            Boolean bit = redisService.getBit(key, userId);
            System.err.println("bit = " + bit);
        }
        Long count = redisService.bitCount(key);
        System.err.println("count = " + count);
    }

    //bitMap求和
    @Test
    public void testBitMapCount() {
        Long l = redisService.bitSum("bitSum", "bitmap1", "bitmap2");
        if (l > 0) {
            Long count = redisService.bitCount("bitSum");
            System.err.println("count = " + count);
        }
    }


    /**
     * @description: HyperLogLog是一种基数估算算法，虽然它能够估计集合中的元素数量，但是它的估计结果是一个近似值，并不是精确的数量
     */
    @Test
    public void testHyperLog() {
        String key = "hyperLog";
        List<Object> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        redisService.setHypeLog(key, list);

        Long count = redisService.hypeLogSize(key);
        System.err.println("count = " + count);

        Long union = redisService.hypeLogUnion("hyperLog", Arrays.asList("hyperLog1", "hyperLog2"));
        System.err.println("union = " + union);
    }


    @Test
    public void del() {
        redisService.del("unionZSet");
    }

    // ---------------------------------caffeineCache---------------------------------


    /**
     * caffeineCache测试类
     */
    @Test
    public void caffeineDemo() {
        //application.yml caffeine.cacheNames名称一致
        String cacheName = "caffeine";
        String key = "nova-cache";
        cacheUtil.put(cacheName, key, "1");

        Object cache = cacheUtil.get(cacheName, key);
        System.err.println("cache = " + cache);
    }

    // ---------------------------------Memcached---------------------------------

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
