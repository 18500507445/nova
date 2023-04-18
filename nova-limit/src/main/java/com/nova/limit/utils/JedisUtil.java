package com.nova.limit.utils;

import cn.hutool.core.util.SerializeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: jedis工具类
 * @author: wzh
 * @date: 2023/4/18 19:53
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JedisUtil {

    private final JedisPool jedisPool;

    /**
     * key 中保存的数字值+1
     *
     * @param key
     * @return long 1-成功 0-失败
     */
    public long incr(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.incr(key);
        } catch (Exception ex) {
            log.error("incr error.", ex);
        }
        return 0L;
    }

    public long incr(String key, long increment) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.incrBy(key, increment);
        } catch (Exception ex) {
            log.error("incr error.", ex);
        }
        return 0L;
    }

    /**
     * key 中保存的数字值-1
     *
     * @param key
     * @return long 1-成功 0-失败
     */
    public long decr(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.decr(key);
        } catch (Exception ex) {
            log.error("incr error.", ex);
        }
        return 0L;
    }

    /**
     * 设置 key 的过期时间，key 过期后将不再可用
     *
     * @param key
     * @param seconds 秒
     * @return long 1-成功 0-失败
     */
    public long expire(String key, long seconds) {
        if (StringUtils.isBlank(key)) {
            return 0L;
        }
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.expire(key, seconds);
        }
    }

    /**
     * 以 UNIX 时间戳(unix timestamp)格式设置 key 的过期时间，过期后将不再可用
     *
     * @param key
     * @param unixTimestamp 时间戳
     * @return long 1-成功 0-失败
     */
    public long expireAt(String key, int unixTimestamp) {
        if (StringUtils.isBlank(key)) {
            return 0L;
        }
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.expireAt(key, unixTimestamp);
        } catch (Exception ex) {
            log.error("expireAt error[key={}, unixTimestamp={}]", key, unixTimestamp, ex);
        }
        return 0L;
    }

    /**
     * ttl命令以秒为单位返回 key 的剩余过期时间
     *
     * @param key
     * @return long
     */
    public Long ttl(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.ttl(key);
        }
    }

    /**
     * 将 string 类型的 value 放到 key 的value上
     * 隐含覆盖，默认的ttl是-1（永不过期）
     *
     * @param key
     * @param value
     * @return boolean true/false
     */
    public boolean set(String key, String value) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set(key, value);
            return true;
        } catch (Exception ex) {
            log.error("set error.", ex);
        }
        return false;
    }

    /**
     * 将 string 类型的 value 放到 key 的value上,并设置过期时间
     *
     * @param key
     * @param value
     * @param seconds
     * @return boolean true/false
     */
    public boolean setEx(String key, String value, long seconds) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.setex(key, seconds, value);
            return true;
        } catch (Exception ex) {
            log.error("set error.", ex);
        }
        return false;
    }

    /**
     * <p>分布式锁</p>
     * not exists 在指定的 key 不存在时，为 key 设置指定的值
     *
     * @param key
     * @param value
     * @param seconds 过期时间/秒
     * @return boolean true/false
     */
    public boolean setNx(String key, String value, long seconds) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            Long setNx = jedis.setnx(key, value);
            if (setNx > 0) {
                jedis.expire(key, seconds);
            }
            return setNx > 0;
        } catch (Exception ex) {
            log.error("setNx error.", ex);
        }
        return false;
    }

    /**
     * 获取指定 key 的值
     *
     * @param key
     * @return String
     */
    public String get(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            log.error("get error.", e);
        }
        return null;
    }

    /**
     * 获取指定 key 的值
     *
     * @param key
     * @param defaultValue key不存在或储存的值不是字符串类型时，返回的默认值
     * @return String
     */
    public String get(String key, String defaultValue) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(key) == null ? defaultValue : jedis.get(key);
        } catch (Exception ex) {
            log.error("get error.", ex);
        }
        return defaultValue;
    }

    /**
     * 删除已存在的 key, 不存在的 key 会被忽略
     *
     * @param key
     * @return boolean
     */
    public boolean del(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.del(key);
            return true;
        } catch (Exception ex) {
            log.error("del error.", ex);
        }
        return false;
    }

    public boolean del(String name, String objectId) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.del((name + ":" + objectId).getBytes());
            return true;
        } catch (Exception ex) {
            log.error("del error.", ex);
        }
        return false;
    }

    /**
     * list 链表头插法 "a","b","c" --> "c","b","a"
     *
     * @param key
     * @param value
     * @return
     */
    public boolean headPush(String key, String... value) {
        if ((key == null) || (value == null)) {
            return false;
        }
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.lpush(key, value);
            return true;
        } catch (Exception ex) {
            log.error("setList error.", ex);
        }
        return false;
    }

    public boolean headPush(String key, long seconds, String... value) {
        boolean result = headPush(key, value);
        if (result) {
            long i = expire(key, seconds);
            return i == 1L;
        }
        return false;
    }

    public boolean headPush(String key, List<String> list) {
        if (StringUtils.isBlank(key) || (list == null) || (list.size() == 0)) {
            return false;
        }
        for (String value : list) {
            headPush(key, value);
        }
        return true;
    }

    /**
     * list 链表尾插法 "a","b","c" --> "a","b","c"
     *
     * @param key
     * @param value
     * @return
     */
    public boolean tailPush(String key, String... value) {
        if (StringUtils.isBlank(key) || (value == null)) {
            return false;
        }
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.rpush(key, value);
            return true;
        } catch (Exception ex) {
            log.error("rpush error.", ex);
        }
        return false;
    }

    public boolean tailPush(String key, long seconds, String... value) {
        boolean result = tailPush(key, value);
        if (result) {
            long i = expire(key, seconds);
            return i == 1L;
        }
        return false;
    }

    public boolean tailPush(String key, List<String> list) {
        if (StringUtils.isBlank(key) || (list == null) || (list.size() == 0)) {
            return false;
        }
        for (String value : list) {
            tailPush(key, value);
        }
        return true;
    }

    /**
     * 移除并返回列表的最后一个元素
     *
     * @param key
     * @return String
     */
    public String removeAndGet(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.rpop(key);
        } catch (Exception ex) {
            log.error("removeAndGet error.", ex);
        }
        return null;
    }

    /**
     * 取 key 链表中的所有元素
     *
     * @param key
     * @return
     */
    public List<String> lrange(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.lrange(key, 0L, -1L);
        } catch (Exception ex) {
            log.error("lrange error.", ex);
        }
        return null;
    }

    /**
     * 获取指定索引处的元素 索引从 0 开始
     *
     * @param key
     * @param index
     * @return
     */
    public String lindex(String key, int index) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.lindex(key, index);
        } catch (Exception ex) {
            log.error("lindex error.", ex);
        }
        return null;
    }

    /**
     * 获取链表元素个数
     *
     * @param key
     * @return
     */
    public long llen(String key) {
        if (StringUtils.isBlank(key)) {
            return 0L;
        }
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.llen(key);
        } catch (Exception ex) {
            log.error("countList error.", ex);
        }
        return 0L;
    }

    /**
     * 获取指定位置到结束位置的链表
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.lrange(key, start, end);
        } catch (Exception ex) {
            log.error("rangeList 出错[key=" + key + " start=" + start +
                    " end=" + end + "]" + ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * 仅保留开始索引至结束索引之间的链表，包含 start 和 end
     *
     * @param key
     * @param start
     * @param end
     * @return String 返回结果链表
     */
    public String ltrim(String key, long start, long end) {
        if (StringUtils.isBlank(key)) {
            return "-";
        }
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.ltrim(key, start, end);
        } catch (Exception ex) {
            log.error("LTRIM 出错[key=" + key + " start=" + start + " end=" +
                    end + "]" + ex.getMessage(), ex);
        }
        return "-";
    }

    /**
     * 从头部(left)向尾部(right)遍历链表，删除count个值为value的元素，返回值为实际删除的数量
     *
     * @param key
     * @param count
     * @param value
     * @return
     */
    public boolean lrem(String key, long count, String value) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.lrem(key, count, value);
            return true;
        } catch (Exception ex) {
            log.error("removeListValue error.", ex);
        }
        return false;
    }

    public int lrem(String key, List<String> values) {
        return lrem(key, 1L, values);
    }

    public int lrem(String key, long count, List<String> values) {
        int result = 0;
        if ((values != null) && (values.size() > 0)) {
            for (String value : values) {
                if (lrem(key, count, value)) {
                    result++;
                }
            }
        }
        return result;
    }


    /**
     * 为哈希表中的字段赋值
     * 将哈希表 key 中的字段 field 的值设为 value
     *
     * @param key
     * @param field
     * @param value
     * @return boolean true/false
     */
    public boolean hashSet(String key, String field, String value) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.hset(key, field, value);
            return true;
        } catch (Exception ex) {
            log.error("hset error.", ex);
        }
        return false;
    }

    /**
     * 返回哈希表中指定字段的值
     *
     * @param key
     * @param field
     * @return String 返回给定字段的值
     */
    public String hashGet(String key, String field) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.hget(key, field);
        } catch (Exception ex) {
            log.error("hget error.", ex);
        }
        return null;
    }

    /**
     * 返回哈希表中，所有的字段和值
     *
     * @param key
     * @return Map<String, String>
     */
    public Map<String, String> hashGetAll(String key) {
        Map<String, String> map = null;
        try (Jedis jedis = this.jedisPool.getResource()) {
            map = jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 用于同时将多个 field-value 对设置到哈希表中
     *
     * @param key
     * @param hash
     * @return boolean
     */
    public boolean hashSet(String key, Map<String, String> hash) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            //String key = name + ":" + objectId;
            jedis.hmset(key, hash);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 返回哈希表中，一个或多个给定字段的值
     *
     * @param key
     * @param fields
     * @return List<String> 一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样
     */
    public List<String> hashGet(String key, String... fields) {
        List<String> list = null;
        try (Jedis jedis = this.jedisPool.getResource()) {
            list = jedis.hmget(key, fields);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略
     *
     * @param key
     * @param fields
     * @return long 被成功删除字段的数量，不包括被忽略的字段
     */
    public long hashDel(String key, String... fields) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.hdel(key, fields);
        } catch (Exception ex) {
            log.error("hdel error.", ex);
        }
        return 0L;
    }

    /**
     * 获取哈希表中字段的数量
     *
     * @param key
     * @return long 哈希表中字段的数量, 当 key 不存在时，返回 0
     */
    public long hashLength(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.hlen(key);
        } catch (Exception ex) {
            log.error("hlen error.", ex);
        }
        return 0L;
    }

    /**
     * 查看哈希表的指定字段是否存在
     *
     * @param key
     * @param field
     * @return boolean
     */
    public boolean hashExists(String key, String field) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.hexists(key, field);
        } catch (Exception ex) {
            log.error("hexists error.", ex);
        }
        return false;
    }

    /**
     * 返回哈希表所有字段的值
     *
     * @param key
     * @return List<String> 一个包含哈希表中所有值的表, 当 key 不存在时, 返回一个空表
     */
    public List<String> getHashValueList(String key) {
        List<String> list = null;
        try (Jedis jedis = this.jedisPool.getResource()) {
            list = jedis.hvals(key);
        } catch (Exception ex) {
            log.error("getHashValueList error.", ex);
        }
        return list;
    }

    /**
     * 获取哈希表中的所有字段名
     *
     * @param key
     * @return Set<String> 包含哈希表中所有字段的列表, 当 key 不存在时, 返回一个空列表
     */
    public Set<String> getHashKeySet(String key) {
        Set<String> set = null;
        try (Jedis jedis = this.jedisPool.getResource()) {
            set = jedis.hkeys(key);
        } catch (Exception ex) {
            log.error("getHashKeySet error.", ex);
        }
        return set;
    }

    public List<Map.Entry<String, String>> hscan(String domain, String match) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            int cursor = 0;
            ScanParams scanParams = new ScanParams();
            scanParams.match(match);
            List list = new ArrayList();
            do {
                ScanResult scanResult = jedis.hscan(domain, String.valueOf(cursor), scanParams);
                list.addAll(scanResult.getResult());
                cursor = Integer.parseInt(scanResult.getCursor());
            } while (cursor > 0);
            return list;
        } catch (Exception ex) {
            log.error("hscan error.", ex);
        }
        return null;
    }


    /**
     * 将一个元素添加到 Set 集合中
     *
     * @param key
     * @param member
     * @return boolean true/false
     */
    public boolean setSet(String key, String member) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.sadd(key, member);
            return true;
        } catch (Exception e) {
            log.error("sadd error", e);
        }
        return false;
    }

    public boolean setSet(String key, long seconds, String member) {
        boolean result = setSet(key, member);
        if (result) {
            long i = expire(key, seconds);
            return i == 1L;
        }
        return false;
    }

    public boolean setSet(String key, List<String> members) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.sadd(key, members.toArray(new String[0]));
            return true;
        } catch (Exception e) {
            log.error("sadd error", e);
        }
        return false;

    }

    /**
     * 返回集合中的所有成员
     *
     * @param key
     * @return Set<String>
     */
    public Set<String> getSetList(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.smembers(key);
        } catch (Exception e) {
            log.error("getSetList error", e);
        }
        return null;
    }

    /**
     * 判断 member 元素是否是集合 key 的成员
     *
     * @param key
     * @param member
     * @return boolean true/false
     */
    public boolean sismember(String key, String member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sismember(key, member);
        } catch (Exception e) {
            log.error("sismember error", e);
        }
        return false;
    }

    /**
     * 返回集合中一个或多个随机数
     *
     * @param key
     * @param count
     * @return
     */
    public List<String> srandmember(String key, int count) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.srandmember(key, count);
        } catch (Exception e) {
            log.error("srandmember error", e);
        }
        return null;
    }

    /**
     * 移除集合中的一个或多个成员元素，不存在的成员元素会被忽略
     *
     * @param key
     * @param members
     * @return long 被成功移除的元素的数量，不包括被忽略的元素
     */
    public long srem(String key, String... members) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.srem(key, members);
        } catch (Exception e) {
            log.error("srem error", e);
        }
        return 0L;
    }

    /**
     * 集合中元素的数量
     *
     * @param key
     * @return long 当集合 key 不存在时，返回 0
     */
    public long scard(String key) {
        if (StringUtils.isBlank(key)) {
            return 0L;
        }
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.scard(key);
        } catch (Exception ex) {
            log.error("countSet error.", ex);
        }
        return 0L;
    }

    /**
     * 返回给定集合之间的差集。不存在的集合 key 将视为空集
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,c,e}
     * SDIFF key1 key2 key3 = {b,d}
     *
     * @param key
     * @return Set<String>
     */
    public Set<String> sdiff(String... key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sdiff(key);
        } catch (Exception e) {
            log.error("sdiffstore error", e);
        }
        return null;
    }

    /**
     * 将给定集合之间的差集存储在指定的集合中。如果指定的集合 dstkey 已存在，则会被覆盖。
     * key1 = {"hello", "foo"}
     * key2 = {"hello", "world"}
     * sdiffstore = {"foo"}
     *
     * @param dstkey
     * @param key
     * @return
     */
    public long sdiffstore(String dstkey, String... key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sdiffstore(dstkey, key);
        } catch (Exception e) {
            log.error("sdiffstore error", e);
        }
        return 0L;
    }

    public boolean zadd(String key, long score, String value) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.zadd(key, score, value);
            return true;
        } catch (Exception ex) {
            log.error("zadd error.", ex);
        }
        return false;
    }

    public long zcount(String key, long startScore, long endScore) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            Long count = jedis.zcount(key, startScore, endScore);
            return count == null ? 0L : count;
        } catch (Exception ex) {
            log.error("zcount error.", ex);
        }
        return 0L;
    }

    public Set<String> zrange(String key, int startRange, int endRange, boolean orderByDesc) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            if (orderByDesc) {
                return jedis.zrevrange(key, startRange, endRange);
            }
            return jedis.zrange(key, startRange, endRange);
        } catch (Exception ex) {
            log.error("zrange error.", ex);
        }
        return null;
    }

    public Set<String> zrangeByScore(String key, long startScore, long endScore, boolean orderByDesc) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            Set localSet;
            if (orderByDesc) {
                return jedis.zrevrangeByScore(key, endScore, startScore);
            }
            return jedis.zrangeByScore(key, startScore, endScore);
        } catch (Exception ex) {
            log.error("zrangeByScore error.", ex);
        }
        return null;
    }

    public Double zscore(String key, String member) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.zscore(key, member);
        } catch (Exception ex) {
            log.error("zscore error.", ex);
        }
        return null;
    }

    public boolean zrem(String key, String... value) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            long count = jedis.zrem(key, value);
            return count > 0L;
        } catch (Exception ex) {
            log.error("zrem error.", ex);
        }
        return false;
    }

    public List<List<String>> getAllLikeKey(String key) {
        List<List<String>> list = new ArrayList<>();
        try (Jedis jedis = this.jedisPool.getResource()) {
            Set<String> keys = jedis.keys(key + "*");
            for (String s : keys) {
                try {
                    List<String> lrange = jedis.lrange(s, 0L, -1L);
                    if (lrange != null && lrange.size() != 0) {
                        list.add(lrange);
                    }
                } catch (Exception ignored) {

                }
            }
        } catch (Exception ex) {
            log.error("getAllLikeKey error.", ex);
        }
        return list;
    }

    public List<String> getPipeValues(String name, String objectId, String... fields) {
        try (Jedis jedis = this.jedisPool.getResource();
             Pipeline pipeline = jedis.pipelined()) {
            String key = name + ":" + objectId;
            Response<List<String>> response = pipeline.hmget(key, fields);
            pipeline.sync();
            return response.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Object加入Redis
     *
     * @param name
     * @param objectId
     * @param <T>
     */
    public <T> void addObject(String name, String objectId, T t) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.set((name + ":" + objectId).getBytes(), SerializeUtil.serialize(t));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Object加入Redis
     *
     * @param name
     * @param objectId
     * @param seconds
     * @param <T>
     */
    public <T> void addObject(String name, String objectId, long seconds, T t) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.setex((name + ":" + objectId).getBytes(), seconds, SerializeUtil.serialize(t));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T getObject(String key) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            byte[] data = jedis.get((key).getBytes());
            return (T) SerializeUtil.deserialize(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从Redis获取Object
     *
     * @param name
     * @param objectId
     * @return
     */
    public <T> T getObject(String name, String objectId) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            String key = name + ":" + objectId;
            byte[] data = jedis.get((key).getBytes());
            return (T) SerializeUtil.deserialize(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
