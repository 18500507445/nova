package com.nova.book.algorithm.list;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

/**
 * @description: 环形链表哨兵
 * @author: wzh
 * @date: 2023/3/7 10:31
 */
class RingLinkedListSentinel {

    private final Node sentinel = new Node(null, -1, null);

    /**
     * 哨兵的上下节点指向都是自己
     */
    public RingLinkedListSentinel() {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    private static class Node {
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
     * 头插 sentinel-->1-->2-->sentinel(是头也是尾)
     */
    public void addFirst(int value) {
        Node a = sentinel;
        Node b = sentinel.next;
        Node added = new Node(a, value, b);
        a.next = added;
        b.prev = added;
    }

    /**
     * 尾插
     */
    public void addLast(int value) {
        Node a = sentinel.prev;
        Node b = sentinel;
        Node added = new Node(a, value, b);
        a.next = added;
        b.prev = added;
    }

    /**
     * 头删 sentinel-->1-->2-->sentinel
     */
    public void removeFirst() {
        Node a = sentinel;
        Node removed = sentinel.next;
        if (removed == sentinel) {
            throw new IllegalArgumentException("不能删除哨兵");
        }
        Node b = removed.next;
        a.next = b;
        b.prev = a;
    }

    /**
     * 尾删
     */
    public void removeLast() {
        Node removed = sentinel.prev;
        if (removed == sentinel) {
            throw new IllegalArgumentException("不能删除哨兵");
        }
        Node a = removed.prev;
        Node b = removed.next;

        a.next = b;
        b.prev = a;
    }

    /**
     * 根据值删除
     *
     * @param value
     */
    public void removeByValue(int value) {
        Node removed = findNode(value);
        if (null == removed) {
            return;
        }
        Node a = removed.prev;
        Node b = removed.next;
        a.next = b;
        b.prev = a;
    }

    public Node findNode(int value) {
        for (Node p = sentinel.next; p != sentinel; p = p.next) {
            if (value == p.value) {
                return p;
            }
        }
        return null;
    }

    public void forEach(Consumer<Integer> consumer) {
        for (Node p = sentinel.next; p != sentinel; p = p.next) {
            consumer.accept(p.value);
        }
    }
}


class RingLinkedListSentinelTest {

    /**
     * 头插测试
     */
    @Test
    public void demoA() {
        RingLinkedListSentinel list = new RingLinkedListSentinel();
        for (int i = 1; i <= 4; i++) {
            list.addFirst(i);
        }
        list.forEach(System.out::println);
    }

    /**
     * 尾插测试
     */
    @Test
    public void demoB() {
        RingLinkedListSentinel list = new RingLinkedListSentinel();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        list.forEach(System.out::println);
    }

    /**
     * 头删除
     */
    @Test
    public void demoC() {
        RingLinkedListSentinel list = new RingLinkedListSentinel();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        list.removeFirst();
        list.removeFirst();
        list.removeFirst();
        list.removeFirst();
        list.forEach(System.out::println);
    }

    /**
     * 尾删除
     */
    @Test
    public void demoD() {
        RingLinkedListSentinel list = new RingLinkedListSentinel();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        list.removeLast();
        list.removeLast();
        list.removeLast();
        list.removeLast();
        list.forEach(System.out::println);
    }

    /**
     * 值删除
     */
    @Test
    public void demoE() {
        RingLinkedListSentinel list = new RingLinkedListSentinel();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        list.removeByValue(4);
        list.forEach(System.out::println);
    }

}

        