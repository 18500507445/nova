package com.nova.book.algorithm.base;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

/**
 * @description: 链表是数据元素的线性集合，其每个元素都指向下一个元素，元素存储上并不连续
 * 链表类型：单项链表（每个元素只知道下一个元素），双向链表（每个元素知道上一个元素和下一个元素），循环链表
 * 性能：查找index时间复杂度O(n)，
 * @author: wzh
 * @date: 2023/3/6 18:04
 */
class SinglyLinkedList {

    /**
     * 头指针
     */
    private Node head = null;

    /**
     * 节点类，因为链表和节点是组合关系，所以当做内部类，对外隐藏实现细节
     */
    private static class Node {

        //值
        int value;

        //下一个节点指针
        Node next;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    /**
     * 向链表头部添加
     *
     * @param value
     */
    public void addFirst(int value) {
        //1.链表为空 头部节点指向第一个节点
        //head = new Node(value, null);

        //2.链表非空 头部节点指向新节点，新节点指向第一个节点
        head = new Node(value, head);
    }

    /**
     * 遍历链表
     */
    public void forEach(Consumer<Integer> consumer) {
        Node p = head;
        while (null != p) {
            consumer.accept(p.value);
            p = p.next;
        }
    }

    public void forEach2(Consumer<Integer> consumer) {
        for (Node p = head; null != p; p = p.next) {
            consumer.accept(p.value);
        }
    }

}

/**
 * 链表测试类
 */
class LinkedTest {

    /**
     * 打印结果3、2、1、0，每次插入都是头部插入链表
     */
    @Test
    public void demoA() {
        SinglyLinkedList list = new SinglyLinkedList();
        for (int i = 0; i < 4; i++) {
            list.addFirst(i);
        }
        list.forEach(System.out::println);
    }

}


