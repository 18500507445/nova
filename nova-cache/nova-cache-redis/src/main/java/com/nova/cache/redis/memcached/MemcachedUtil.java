package com.nova.cache.redis.memcached;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: Memcache工具类
 * @author: wzh
 * @date: 2022/12/20 15:40
 */
@Component
public class MemcachedUtil implements Serializable {

    /**
     * 默认存储14天
     * 1000毫秒开始
     */
    private final static int DEFAULT_TIME = 1000 * 60 * 60 * 24 * 14;

    protected static MemCachedClient MC = new MemCachedClient();

    private static final String SERVERS_URL = "ip1:port,ip2:port";

    private MemcachedUtil() {
        String[] cacheServers = SERVERS_URL.split(",");
        String[] serverList = new String[cacheServers.length];
        System.arraycopy(cacheServers, 0, serverList, 0, cacheServers.length);
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(serverList);
        pool.setInitConn(5);
        pool.setMinConn(2);
        pool.setMaxConn(200);
        pool.initialize();
    }

    public void createData(String key, Object value) {
        Date date = new Date(System.currentTimeMillis() + DEFAULT_TIME);
        MC.set(key, value, date);
    }

    public void createData(String key, Object value, long expire) {
        Date date = new Date(System.currentTimeMillis() + expire);
        MC.set(key, value, date);
    }

    public void del(String key) {
        MC.delete(key);
    }

    public Object getData(String key) {
        return MC.get(key);
    }
}
