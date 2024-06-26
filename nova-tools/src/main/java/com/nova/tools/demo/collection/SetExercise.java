package com.nova.tools.demo.collection;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Set集合 无序、值不重复
 * @author: wzh
 * @date: 2023/4/4 22:04
 */
class SetExercise {

    /**
     * 线程：不安全
     * 性能：很好
     * 场景：底层基于HashMap实现，无序
     */
    @Test
    public void hashSet() {
        HashSet<String> hashSet = new HashSet<>();
    }

    /**
     * 使用二叉树的原理对新 add()的对象按照指定的顺序排序（升序、降序）
     * Integer和String对象都可以进行默认的 TreeSet 排序
     */
    @Test
    public void treeSet() {
        TreeSet<String> treeSet = new TreeSet<>();
    }

    /**
     * 继承HashSet，实现LinkedHashMap
     * 特点：有序的，遍历该集合时候将会以元素的添加顺序访问集合的元素
     * 线程不安全
     */
    @Test
    public void linkHashSet() {
        final LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
    }

    /**
     * 线程安全的hashSet
     */
    @Test
    public void safeHashSet() {
        Set<String> hashSet = ConcurrentHashMap.newKeySet();
    }

}
