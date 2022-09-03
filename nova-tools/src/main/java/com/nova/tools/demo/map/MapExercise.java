package com.nova.tools.demo.map;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.nova.tools.demo.entity.GroupPeople;
import com.nova.tools.demo.entity.Myself;
import com.nova.tools.demo.entity.People;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/4/22 11:32
 */

public class MapExercise {
    public static void main(String[] args) {
//        forEachMap();
        treeMap();
//        forEachMapOld();
    }

    /**
     * Java 7 forEachMap
     */
    private static void forEachMapOld() {
        List<People> peopleResult = Myself.EXERCISE_LIST;
        HashMap<Integer, List<People>> map = new HashMap<>(16);
        if (CollUtil.isNotEmpty(peopleResult)) {
            for (People people : peopleResult) {
                if (null != people.getGroupId()) {
                    List<People> peopleList = map.get(people.getGroupId());
                    if (CollectionUtils.isEmpty(peopleList)) {
                        peopleList = new ArrayList<>();
                        peopleList.add(people);
                        map.put(people.getGroupId(), peopleList);
                    } else {
                        peopleList.add(people);
                    }
                }
            }
        }
        for (Map.Entry<Integer, List<People>> groupMap : map.entrySet()) {
            Integer key = groupMap.getKey();
            List<People> people = map.get(key);
            System.out.println(JSONObject.toJSONString(people));
        }
    }


    /**
     * Java 8 forEachMap
     */
    private static void forEachMap() {
        List<People> peopleResult = Myself.EXERCISE_LIST;

        List<GroupPeople> groupList = new ArrayList<>();

        HashMap<Integer, String> map = new HashMap<>(16);

        peopleResult.forEach(people -> {
            map.put(people.getGroupId(), people.getGroupName());
        });

        //Map<Integer, String> map = peopleResult.stream().collect(toMap(People::getGroupId, People::getGroupName));

        Map<Integer, List<People>> group = peopleResult.stream().collect(Collectors.groupingBy(People::getGroupId));
        for (Map.Entry<Integer, List<People>> next : group.entrySet()) {
            GroupPeople groupPeople = new GroupPeople();
            groupPeople.setGroupId(next.getKey());
            groupPeople.setGroupName(map.get(next.getKey()));
            List<People> peopleList = new ArrayList<>();
            next.getValue().forEach(people -> {
                People peopleData = new People();
                peopleData.setId(people.getId());
                peopleData.setAge(people.getAge());
                peopleData.setName(people.getName());
                peopleData.setDescription(people.getDescription());
                peopleList.add(peopleData);

            });
            groupPeople.setListPeople(peopleList);
            groupList.add(groupPeople);
        }

        System.out.println(JSONObject.toJSONString(groupList));
    }


    private static void hashMap() {
        /**
         *  数组方式存储key/value，线程非安全，允许null作为key和value，key不可以重复，value允许重复，
         *  不保证元素迭代顺序是按照插入时的顺序，key的hash值是先计算key的hashcode值，然后再进行计算，
         *  每次容量扩容会重新计算所以key的hash值，会消耗资源，要求key必须重写equals和hashcode方法.
         *  默认初始容量16，加载因子0.75，扩容为旧容量乘2，查找元素快，如果key一样则比较value，如果value不一样，则按照链表结构存储value，就是一个key后面有多个value；
         *
         *  如果按照key的顺序 先进先出  那么用 LinkedHashMap
         */
        Map<String, String> map = new HashMap<>(16);
        map.put("1", "a");
        map.put("2", "b");
        map.put("3", "c");

        //第一种：普遍使用，二次取值
        System.out.println("通过Map.keySet遍历key和value：");
        for (String key : map.keySet()) {
            System.out.println("key= " + key + " and value= " + map.get(key));
        }

        //第二种
        System.out.println("通过Map.entrySet使用iterator遍历key和value：");
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        //第三种：推荐，尤其是容量大时
        System.out.println("通过Map.entrySet遍历key和value");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        //第四种
        System.out.println("通过Map.values()遍历所有的value，但不能遍历key");
        for (String v : map.values()) {
            System.out.println("value= " + v);
        }
    }

    private static void treeMap() {
        /**
         * 基于红黑二叉树的NavigableMap的实现，线程非安全，不允许null，key不可以重复，value允许重复，
         * 存入TreeMap的元素应当实现Comparable接口或者实现Comparator接口，会按照排序后的顺序迭代元素，
         * 两个相比较的key不得抛出classCastException。主要用于存入元素的时候对元素进行自动排序，迭代输出的时候就按排序顺序输出
         *
         * TreeMap 是一个有序的key-value集合，它是通过红黑树实现的
         * 继承于AbstractMap，所以它是一个Map，即一个key-value集合
         * 实现了NavigableMap接口，意味着它支持一系列的导航方法，比如返回有序的key集合
         * 实现了Cloneable接口，意味着它能被克隆
         * 实现了Serializable接口，意味着它支持序列化
         */
        Map<String, String> map = new TreeMap<>();
        map.put("1", "a");
        map.put("3", "b");
        map.put("2", "c");

        System.out.println("通过Map.entrySet遍历key和value");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        System.out.println("treeMap降序排列,+降序，-正序");
        Map<String, String> mapNew = new TreeMap<>((o1, o2) -> +o2.compareTo(o1));
        mapNew.putAll(map);
        for (Map.Entry<String, String> entry : mapNew.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

    }

    private static void hashTable() {
        /**
         *key和value都不允许为null,方法是同步的，有人建议，涉及多线程使用时候，使用hashTable
         */
        Hashtable<String, String> hashTable = new Hashtable<>();
        hashTable.put("1", "a");
        hashTable.put("2", "b");
        hashTable.put("3", "c");
        for (Map.Entry<String, String> entry : hashTable.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
    }
}
