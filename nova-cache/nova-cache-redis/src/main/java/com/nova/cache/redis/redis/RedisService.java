package com.nova.cache.redis.redis;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description: redis工具类
 * @author: wzh
 * @date: 2022/6/19 15:40
 */
@Getter
@Slf4j
@Component
public class RedisService {

    private static final String LOCK_PREFIX = "redisLock";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public void expire(String key, Long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }
    }

    /**
     * 添加到带有过期时间的缓存
     *
     * @param key      redis主键
     * @param value    值
     * @param time     过期时间
     * @param timeUnit 过期时间单位
     */
    public void setExpire(final String key, final Object value, final long time, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public void setExpire(final String key, final Object value, final long time, final TimeUnit timeUnit, RedisSerializer<Object> valueSerializer) {
        byte[] rawKey = rawKey(key);
        byte[] rawValue = rawValue(value, valueSerializer);

        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(@NotNull RedisConnection connection) throws DataAccessException {
                potentiallyUsePsetEx(connection);
                return null;
            }

            public void potentiallyUsePsetEx(RedisConnection connection) {
                if (!TimeUnit.MILLISECONDS.equals(timeUnit) || !failsafeInvokePsetEx(connection)) {
                    connection.setEx(rawKey, TimeoutUtils.toSeconds(time, timeUnit), rawValue);
                }
            }

            private boolean failsafeInvokePsetEx(RedisConnection connection) {
                boolean failed = false;
                try {
                    connection.pSetEx(rawKey, time, rawValue);
                } catch (UnsupportedOperationException e) {
                    failed = true;
                }
                return !failed;
            }
        }, true);
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键 不能为 null
     * @return 时间(秒) 返回 0代表为永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key获取对象
     *
     * @param key             the key
     * @param valueSerializer 序列化
     * @return the string
     */
    public Object get(final String key, RedisSerializer<Object> valueSerializer) {
        byte[] rawKey = rawKey(key);
        return redisTemplate.execute(connection -> deserializeValue(connection.get(rawKey), valueSerializer), true);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public Boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public Boolean set(String key, Object value, Long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @return true成功 false 失败
     */
    public Boolean set(String key, Object value, Duration timeout) {
        try {
            Assert.notNull(timeout, "Timeout must not be null!");
            if (TimeoutUtils.hasMillis(timeout)) {
                redisTemplate.opsForValue().set(key, value, timeout.toMillis(), TimeUnit.MILLISECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value, timeout.getSeconds(), TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return Long
     */
    public Long incr(String key, Long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return Long
     */
    public Long decr(String key, Long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为 null
     * @param item 项 不能为 null
     * @return 值
     */
    public Object getHashValue(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取 hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> getHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public Boolean setHash(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public Boolean setHash(String key, Map<String, Object> map, Long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public Boolean setHash(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public Boolean setHash(String key, String item, Object value, Long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为 null
     * @param item 项 可以使多个不能为 null
     */
    public void delHash(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为 null
     * @param item 项 不能为 null
     * @return true 存在 false不存在
     */
    public Boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return Double
     */
    public Double hashIncr(String key, String item, Double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return Double
     */
    public Double hashDecr(String key, String item, Double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 根据 key获取 Set中的所有值
     *
     * @param key 键
     * @return Set
     */
    public Set<Object> getSet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean containsSet(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return 0L;
        }
    }

    public Long setSet(String key, Collection<Object> values) {
        try {
            return redisTemplate.opsForSet().add(key, values.toArray());
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return 0L;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setSet(String key, Long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return 0L;
        }
    }

    public Long setSet(String key, Long time, Collection<Object> values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values.toArray());
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return Long
     */
    public Long getSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return 0L;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return 0L;
        }
    }

    /**
     * 随机移除key对应集合中的count个元素
     *
     * @param key
     * @param count
     */
    public List<Object> setPop(String key, Long count) {
        return redisTemplate.opsForSet().pop(key, count);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return List
     */
    public List<Object> getList(String key, Long start, Long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return Long
     */
    public Long getListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return 0L;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；
     *              index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return Object
     */
    public Object getListByIndex(String key, Long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 将list放入缓存，右管道进入
     *
     * @param key   键
     * @param value 值
     * @return Boolean
     */
    public Boolean setList(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从管道左边取出并移除
     */
    public Object listPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return Boolean
     */
    public Boolean setList(String key, Object value, Long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return Boolean
     */
    public Boolean setList(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return Boolean
     */
    public Boolean setList(String key, List<Object> value, Long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return Boolean
     */
    public Boolean updateListByIndex(String key, Long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long listRemove(String key, Long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return 0L;
        }
    }

    /**
     * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
     *
     * @param key             the key
     * @param start           the start
     * @param end             the end
     * @param valueSerializer 序列化
     * @return the list
     */
    public List<Object> getList(String key, int start, int end, RedisSerializer<Object> valueSerializer) {
        byte[] rawKey = rawKey(key);
        return redisTemplate.execute(connection -> deserializeValues(connection.lRange(rawKey, start, end), valueSerializer), true);
    }

    public Double setZSet(String key, String value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     */
    public JSONArray getZSet(String key, Long start, Long end) {
        final Set<ZSetOperations.TypedTuple<Object>> zSet = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        return JSONArray.parseArray(JSONObject.toJSONString(zSet));
    }

    /**
     * 把key和otherKey进行合并，目标target
     *
     * @param key
     * @param otherKey
     * @param target
     */
    public void unionZSet(String key, String otherKey, String target) {
        redisTemplate.opsForZSet().unionAndStore(key, otherKey, target);
    }

    /**
     * 把key和otherKeyList合并，目标target
     *
     * @param keys
     * @param target
     */
    public void unionZSetList(String key, List<String> keys, String target) {
        redisTemplate.opsForZSet().unionAndStore(key, keys, target);
    }

    /**
     * 使用redis保证原子操作（判断是否存在，添加key，设置过期时间）
     * 也可以使用 lua 脚本 "return redis.call('set',KEYS[1], ARGV[1],'NX','PX',ARGV[2])"
     * 问题：如果key过期的时间早于程序执行时间，会导致删除key失败，多应用间释放锁，导致锁一直失效；
     * <p>
     * 解决办法：String requestId = UUID.randomUUID().toString().replace("-", "");
     * <p>
     * 固定的key，正常情况加锁、解锁需要自己线程完成才能保证并发情况下的问题的发生（例如库存的超卖），
     * 如果有特殊情况不需要线程与线程间的锁那么共享一把，当前服务其它的线程或者其它服务的线程把锁释放了也没有关系，本质上就是降低请求次数
     *
     * @param requestId
     * @param expireTime
     * @return
     */
    public boolean lock(String requestId, int expireTime) {
        while (true) {
            if (Boolean.TRUE.equals(redisTemplate.boundValueOps(LOCK_PREFIX).
                    setIfAbsent(requestId, expireTime, TimeUnit.SECONDS))) {
                return true;
            }
        }
    }

    /**
     * 将锁释放掉
     * <p>
     * 为何解锁需要校验 requestId 因为不是自己的锁不能释放
     * 客户端A加锁，一段时间之后客户端A解锁，在执行 lock 之前，锁突然过期了。
     * 此时客户端B尝试加锁成功，然后客户端A再执行 unlock 方法，则将客户端B的锁给解除了。
     *
     * @return true false
     */
    public boolean unlock(String requestId) {
        // 这里使用Lua脚本保证原子性操作
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long res = redisTemplate.execute(redisScript, Collections.singletonList(LOCK_PREFIX), requestId);
        return new Long(1).equals(res);
    }

    /**
     * 执行lua脚本
     *
     * @param script
     * @param key
     * @param <T>
     */
    public <T> T execute(RedisScript<T> script, String key) {
        return redisTemplate.execute(script, Collections.singletonList(key), Collections.EMPTY_LIST);
    }

    public <T> T execute(RedisScript<T> script, String key, List<String> args) {
        return redisTemplate.execute(script, Collections.singletonList(key), args);
    }

    /**
     * 设置标记位BitMap
     *
     * @param key    key
     * @param offset 偏移量
     * @param tag    设置为1(设置0表示抹去标记)
     * @description: 应用场景（1）点赞（2）日活（3）用户在线状态
     */
    public Boolean setBit(String key, long offset, boolean tag) {
        return redisTemplate.opsForValue().setBit(key, offset, tag);
    }

    /**
     * 获取BitMap
     *
     * @param key    key
     * @param offset 偏移量
     */
    public Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 统计计数
     *
     * @param key key
     */
    public Long bitCount(String key) {
        return redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.bitCount(key.getBytes()));
    }

    /**
     * 向HyperLogLog中添加一个或多个元素
     */
    public Long setHypeLog(String key, List<Object> value) {
        return redisTemplate.opsForHyperLogLog().add(key, value.toArray());
    }

    //获取给定HyperLogLog的基数估计值
    public Long hypeLogSize(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

    /**
     * 计算多个HyperLogLog的并集，并返回基数估计值
     *
     * @param key
     * @param values
     */
    public Long hypeLogUnion(String key, List<String> values) {
        return redisTemplate.opsForHyperLogLog().union(key, values.toArray(new String[0]));
    }

    public void hypeLogDel(String key) {
        redisTemplate.opsForHyperLogLog().delete(key);
    }

    /**
     * redis 模糊匹配结果集
     *
     * @param key 键
     * @return List
     */
    public Set<String> getKeys(String key) {
        return redisTemplate.keys(key + "*");
    }

    private byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");

        if (key instanceof byte[]) {
            return (byte[]) key;
        }
        RedisSerializer<Object> redisSerializer = (RedisSerializer<Object>) redisTemplate.getKeySerializer();
        return redisSerializer.serialize(key);
    }

    private byte[] rawValue(Object value, RedisSerializer valueSerializer) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }

        return valueSerializer.serialize(value);
    }

    private List deserializeValues(List<byte[]> rawValues, RedisSerializer<Object> valueSerializer) {
        if (valueSerializer == null) {
            return rawValues;
        }
        return SerializationUtils.deserialize(rawValues, valueSerializer);
    }

    private Object deserializeValue(byte[] value, RedisSerializer<Object> valueSerializer) {
        if (valueSerializer == null) {
            return value;
        }
        return valueSerializer.deserialize(value);
    }
}
