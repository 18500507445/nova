package com.nova.tools.demo.collection;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @description: map
 * @author: wzh
 * @date: 2019/4/22 11:32
 */
class MapExercise {

    /**
     * 初始化容量的影响
     */
    @Test
    public void capacity() {
        TimeInterval timer = DateUtil.timer();
        //定义数据量
        int aHundredMillion = 1_000_000;

        Map<Integer, Integer> map1 = new HashMap<>(16);
        for (int i = 0; i < aHundredMillion; i++) {
            map1.put(i, i);
        }
        System.err.println("未初始化容量，耗时 ： " + timer.interval() + " ms");
        timer.restart();

        Map<Integer, Integer> map2 = new HashMap<>(aHundredMillion);
        for (int i = 0; i < aHundredMillion; i++) {
            map2.put(i, i);
        }
        System.err.println("初始化容量为10000000，耗时 ： " + timer.interval() + " ms");
    }

    /**
     * 无序，线程不安全
     * 数组方式存储key/value，都可以为null，key不可以重复，value可重复
     * key的hash值是先计算key的hashcode值，然后再进行计算，
     * 每次容量扩容会重新计算所以key的hash值，会消耗资源，要求key必须重写equals和hashcode方法
     * 默认初始容量16，负载因子0.75，扩容为旧容量乘2，查找元素快，如果key一样则比较value，如果value不一样，则按照链表结构存储value，就是一个key后面有多个value；
     */
    @Test
    public void hashMap() {
        Map<String, String> map = new HashMap<>(16);
        map.put("1", "a");
        map.put("3", "c");
        map.put("2", "b");

        //第一种：普遍使用，二次取值
        System.err.println("通过Map.keySet遍历key和value：");
        for (String key : map.keySet()) {
            System.err.println("key= " + key + " and value= " + map.get(key));
        }

        //第二种
        System.err.println("通过Map.entrySet使用iterator遍历key和value：");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.err.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        //第三种：推荐，尤其是容量大时
        System.err.println("通过Map.entrySet遍历key和value");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.err.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        //第四种
        System.err.println("通过Map.values()遍历所有的value，但不能遍历key");
        for (String v : map.values()) {
            System.err.println("value= " + v);
        }
    }


    /**
     * TreeMap，有序，默认是按键值的升序排序，它是通过红黑树实现的
     * 线程不安全
     */
    @Test
    public void treeMap() {
        Map<String, String> map = new TreeMap<>();
        map.put("1", "a");
        map.put("3", "b");
        map.put("2", "c");

        System.err.println("通过Map.entrySet遍历key和value");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.err.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        System.err.println("treeMap降序排列,+降序，-正序");
        Map<String, String> mapNew = new TreeMap<>((o1, o2) -> +o2.compareTo(o1));
        mapNew.putAll(map);
        for (Map.Entry<String, String> entry : mapNew.entrySet()) {
            System.err.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
    }

    /**
     * 关联数组、哈希表、有序（可以记录元素存储的顺序，FIFO）
     * 线程不安全
     */
    @Test
    public void linkedHashMap() {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("1", "a");
        linkedHashMap.put("3", "c");
        linkedHashMap.put("2", "b");
        for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
            System.err.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
    }


    /**
     * 线程安全（性能较好，sync锁住数组元素）
     */
    @Test
    public void concurrentHashMap() {
        Map<String, String> concurrentHashMap = new ConcurrentHashMap<>(16);
    }


    /**
     * 线程安全
     * 有序
     * 性能最好，没有锁，依靠cas（compare and swap），有就替换
     * 并发跳跃表
     */
    @Test
    public void demoA() {
        ConcurrentSkipListMap<String, String> skipListMap = new ConcurrentSkipListMap<>();
        skipListMap.put("1", "a");
        skipListMap.put("2", "b");
        skipListMap.put("3", "c");
        System.out.println("skipListMap = " + JSONUtil.toJsonStr(skipListMap));
    }


    /**
     * 线程安全
     * 不建议用（性能极低，sync锁住整个对象，效率低下）
     * key和value都不允许为null
     */
    @Test
    @Deprecated
    public void hashTable() {
        Hashtable<String, String> hashTable = new Hashtable<>();
        hashTable.put("1", "a");
        hashTable.put("2", "b");
        hashTable.put("3", "c");
        for (Map.Entry<String, String> entry : hashTable.entrySet()) {
            System.err.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
    }

}
