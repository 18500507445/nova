package com.nova.tools.java8.concurrent;


import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

/**
 * @description: 并发hashMap
 * @author: wzh
 * @date: 2022/11/18 14:54
 */
class ConcurrentExample {

    private static final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(16);

    @Test
    public void testReduce() {
        System.err.println("Parallelism: " + ForkJoinPool.getCommonPoolParallelism());

        map.putIfAbsent("foo", "bar");
        map.putIfAbsent("han", "solo");
        map.putIfAbsent("r2", "d2");
        map.putIfAbsent("c3", "p0");

        String reduced = map.reduce(1, (key, value) -> key + "=" + value,
                (s1, s2) -> s1 + ", " + s2);

        System.err.println(reduced);
    }

    @Test
    public void testSearch() {
        map.putIfAbsent("foo", "bar");
        map.putIfAbsent("han", "solo");
        map.putIfAbsent("r2", "d2");
        map.putIfAbsent("c3", "p0");

        System.err.println("\nsearch()\n");

        String result1 = map.search(1, (key, value) -> {
            System.err.println(Thread.currentThread().getName());
            if (key.equals("foo") && value.equals("bar")) {
                return "foobar";
            }
            return null;
        });

        System.err.println(result1);

        System.err.println("\nsearchValues()\n");

        String result2 = map.searchValues(1, value -> {
            System.err.println(Thread.currentThread().getName());
            if (value.length() > 3) {
                return value;
            }
            return null;
        });

        System.err.println(result2);
    }

    @Test
    public void testForEach() {
        map.putIfAbsent("foo", "bar");
        map.putIfAbsent("han", "solo");
        map.putIfAbsent("r2", "d2");
        map.putIfAbsent("c3", "p0");

        map.forEach(1, (key, value) -> System.err.printf("key: %s; value: %s; thread: %s\n", key, value, Thread.currentThread().getName()));
//        map.forEach(5, (key, value) -> System.err.printf("key: %s; value: %s; thread: %s\n", key, value, Thread.currentThread().getName()));

        System.err.println(map.mappingCount());
    }
}
