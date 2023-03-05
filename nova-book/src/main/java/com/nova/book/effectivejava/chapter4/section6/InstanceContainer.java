package com.nova.book.effectivejava.chapter4.section6;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 22:09
 */
public class InstanceContainer {

    private InstanceContainer() {

    }

    private static final Map<Class<?>, Object> CONTAINER = new ConcurrentHashMap<>();

    public static <T> void putInstance(Class<T> tClass, T instance) {
        CONTAINER.put(tClass, instance);
    }

    public static <T> T getInstance(Class<T> tClass) {
        return tClass.cast(CONTAINER.get(tClass));
    }

}
