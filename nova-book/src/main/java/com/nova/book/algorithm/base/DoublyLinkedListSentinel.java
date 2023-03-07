package com.nova.book.algorithm.base;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

/**
 * @description: 双向链表哨兵模式
 * @author: wzh
 * @date: 2023/3/7 09:32
 */
public class DoublyLinkedListSentinel {

    private final Node head;

    private final Node tail;

    private static class Node {
        //上一个节点指针
        Node prev;

        int value;

        Node next;

        public Node(Node prev, int value, Node next) {
            this.prev = prev;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * 哨兵模式 给定初始值，头下一个节点指向尾，尾的上一个节点指向头
     */
    public DoublyLinkedListSentinel() {
        head = new Node(null, -1, null);
        tail = new Node(null, 999, null);
        head.next = tail;
        tail.prev = head;
    }

    /**
     * 从哨兵开始做起点-1开始查找
     *
     * @param index
     * @return
     */
    private Node findNode(int index) {
        int i = -1;
        for (Node p = head; p != tail; p = p.next, i++) {
            if (i == index) {
                return p;
            }
        }
        return null;
    }

    public void addFirst(int value) {
        insert(0, value);
    }

    public void removeFirst(int value) {
        remove(0);
    }

    /**
     * 尾插入
     *
     * @param value
     */
    public void addLast(int value) {
        Node last = tail.prev;
        //被添加的上一个节点就是last，下一个节点就是尾哨兵
        Node added = new Node(last, value, tail);
        //上一个节点指向被添加
        last.next = added;
        //尾哨兵的上一个节点指向被添加
        tail.prev = added;
    }

    /**
     * 尾删除
     *
     * @param value
     */
    public void removeLast(int value) {
        //哨兵的上一个节点就是被删除的节点
        Node removed = tail.prev;
        if (null == removed) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", 0));
        }
        Node prev = removed.prev;
        //被删除的指针指向尾哨兵
        prev.next = tail;
        //尾哨兵的上一个节点指向prev
        tail.prev = prev;
    }

    /**
     * 索引插入
     *
     * @param index
     * @param value
     */
    public void insert(int index, int value) {
        Node prev = findNode(index - 1);
        if (null == prev) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        Node next = prev.next;
        //被插入的上一个节点是prev，下一个节点是next
        Node inserted = new Node(prev, value, next);
        //上一个节点指向被插入节点
        prev.next = inserted;
        //下一个节点的上一个节点指向被插入节点
        next.prev = inserted;
    }

    /**
     * 索引删除
     * 上一个节点指针指向next
     * next的上一个节点指针指向 prev
     *
     * @param index
     */
    public void remove(int index) {
        Node prev = findNode(index - 1);
        if (null == prev) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        Node removed = prev.next;
        //待删除元素是尾哨兵
        if (removed == tail) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        Node next = removed.next;
        prev.next = next;
        next.prev = prev;
    }

    public void forEach(Consumer<Integer> consumer) {
        for (Node p = head.next; p != tail; p = p.next) {
            consumer.accept(p.value);
        }
    }
}

class DoublyLinkedListTest {

    /**
     * 头插测试
     */
    @Test
    public void demoA() {
        DoublyLinkedListSentinel list = new DoublyLinkedListSentinel();
        for (int i = 1; i <= 4; i++) {
            list.addFirst(i);
        }
        list.forEach(System.out::println);
    }

    /**
     * 下标插入测试
     */
    @Test
    public void demoD() {
        DoublyLinkedListSentinel list = new DoublyLinkedListSentinel();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        list.insert(4, 4);
        list.forEach(System.out::println);
    }

}
