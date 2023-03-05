package com.nova.book.effectivejava.chapter4.section2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 15:51
 */
class Cache {

    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    public static void add(String key, Object v) {
        CACHE.put(key, v);
    }

    /**
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) CACHE.get(key);
    }
}
