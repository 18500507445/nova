package com.nova.tools.demo.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @description: 常见的list实现类
 * @author: wzh
 * @date: 2023/4/4 21:45
 */
class ListExercise {

    /**
     * 线程：不安全
     * 性能：很好
     * 场景：数组实现的，随机查找和遍历效率高，插入、删除效率低。有序、可重复
     */
    @Test
    public void arrayList() {
        List<String> list = new ArrayList<>();
        System.out.println("list = " + list);
    }

    /**
     * 线程：安全
     * 性能：性能略低
     * 场景：与arrayList相似
     */
    @Test
    public void vector() {
        List<String> vector = new Vector<>();
    }

    /**
     * 线程：不安全
     * 性能：双向循环链表实现，头插、删除效率高、查找和便利效率低
     * 场景：可以当做堆栈、队列（单项、双向）使用
     */
    @Test
    public void linkList() {
        LinkedList<String> list = new LinkedList<>();
        list.addFirst("1");
        list.addLast("2");
        System.err.println("linkedList = " + list);
    }

    /**
     * 线程：安全
     * 性能：很好
     * 场景：LinkedList如何进行线程安全处理，方法一：Collections.synchronizedList(new LinkedList())但是不推荐，用下列的代码吧
     */
    @Test
    public void concurrentLinkedQueue() {
        ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<>();
    }

    /**
     * 线程：安全
     * 性能：很好
     * 场景：适用于多读，少写的
     */
    @Test
    public void copyOnWriteList() {
        List<String> list = new CopyOnWriteArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        System.err.println("list = " + list);
    }

}
