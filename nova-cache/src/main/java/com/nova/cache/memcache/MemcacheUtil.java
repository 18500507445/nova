package com.nova.cache.memcache;

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
public class MemcacheUtil implements Serializable {

    /**
     * 默认存储14 天
     */
    private final static int DEFAULT_TIME = 1209600000;

    protected static MemCachedClient MC = new MemCachedClient();

    private static final String SERVERS_URL = "ip1:port,ip2:port";

    private MemcacheUtil() {
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
        return (Object) MC.get(key);
    }
}
