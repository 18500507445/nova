package com.nova.book.algorithm.base;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

/**
 * @description: 单向链表哨兵版
 * @author: wzh
 * @date: 2023/3/6 18:04
 */
class SinglyLinkedSentinelList {

    /**
     * 头指针指向哨兵节点（值无所谓），下次判断Node ==null 的时候可以去掉，因为肯定不为null了
     */
    private Node head = new Node(-1, null);

    /**
     * 头插法
     *
     * @param value
     */
    public void addFirst(int value) {
        insert(0, value);
    }

    /**
     * 遍历链表（从哨兵节点之后开始遍历）
     */
    public void forEach(Consumer<Integer> consumer) {
        Node p = head.next;
        while (null != p) {
            consumer.accept(p.value);
            p = p.next;
        }
    }

    /**
     * 循环遍历找到最后一个节点
     *
     * @param value
     * @return
     */
    private Node findLast(int value) {
        Node p = head;
        while (null != p.next) {
            p = p.next;
        }
        return p;
    }

    /**
     * 尾插
     *
     * @param value
     */
    public void addLast(int value) {
        Node last = findLast(value);
        last.next = new Node(value, null);
    }

    /**
     * 根据下标找节点(从哨兵开始匹配)
     *
     * @param index
     * @return
     */
    private Node findNode(int index) {
        int i = -1;
        for (Node p = head; p != null; p = p.next, i++) {
            if (i == index) {
                return p;
            }
        }
        return null;
    }

    /**
     * 根据下标找value
     *
     * @param index
     * @return
     */
    public int get(int index) {
        Node node = findNode(index);
        if (null == node) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        return node.value;
    }

    /**
     * 下标插入法
     *
     * @param index
     * @param value
     */
    public void insert(int index, int value) {
        Node prev = findNode(index - 1);
        if (null == prev) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        prev.next = new Node(value, prev.next);
    }

    /**
     * 删除首个
     */
    public void removeFirst() {
        remove(0);
    }

    /**
     * 索引删除
     * 下标的前一个节点指向删除的对象
     *
     * @param index
     */
    public void remove(int index) {
        Node prev = findNode(index - 1);
        if (null == prev) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        Node remove = prev.next;
        if (null == remove) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        prev.next = remove.next;
    }

    private static class Node {
        int value;
        Node next;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

}

/**
 * 哨兵链表测试类
 */
class LinkedSentinelTest {

    /**
     * 尾插测试
     */
    @Test
    public void demoA() {
        SinglyLinkedSentinelList list = new SinglyLinkedSentinelList();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        list.forEach(System.out::println);
    }

    /**
     * get
     */
    @Test
    public void demoB() {
        SinglyLinkedSentinelList list = new SinglyLinkedSentinelList();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        int i = list.get(2);
        System.out.println("i = " + i);
    }

    /**
     * 下标插入
     */
    @Test
    public void demoC() {
        SinglyLinkedSentinelList list = new SinglyLinkedSentinelList();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        list.insert(0, 5);
        list.forEach(System.out::println);
    }

    /**
     * 头插
     */
    @Test
    public void demoD() {
        SinglyLinkedSentinelList list = new SinglyLinkedSentinelList();
        for (int i = 1; i <= 4; i++) {
            list.addFirst(i);
        }
        list.forEach(System.out::println);
    }


}


