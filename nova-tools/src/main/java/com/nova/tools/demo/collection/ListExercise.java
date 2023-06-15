package com.nova.tools.demo.collection;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @description: 常见的list实现类
 * @author: wzh
 * @date: 2023/4/4 21:45
 */
class ListExercise {

    /**
     * 数组实现的，随机查找和遍历效率高，插入、删除效率低
     * 有序、可重复、线程不安全
     */
    @Test
    public void arrayList() {
        List<String> arrayList = new ArrayList<>();
    }

    /**
     * 与arrayList相似
     * 线程安全、性能略低
     */
    @Test
    public void vector() {
        List<String> vector = new Vector<>();
    }

    /**
     * 双向循环链表实现，插入、删除效率高、查找和便利效率低
     * 可以当做堆栈、队列（单项、双向）使用，线程不安全
     */
    @Test
    public void linkList() {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.addFirst("1");
        linkedList.addLast("2");
    }

    /**
     * LinkedList如何进行线程安全处理
     * 方法一：Collections.synchronizedList(new LinkedList())但是不推荐，用下列的代码吧
     */
    @Test
    public void concurrentLinkedQueue() {
        ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<>();
    }

    /**
     * 适用于多读，少写的场景，性能很好
     */
    @Test
    public void copyOnWriteList() {
        List<String> copyOnWriteList = new CopyOnWriteArrayList<>();
        copyOnWriteList.add("a");
        copyOnWriteList.add("b");
        copyOnWriteList.add("c");

        System.out.println(JSONUtil.toJsonStr(copyOnWriteList));
    }

}
