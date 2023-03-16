package com.nova.book.algorithm.queqe;

import java.util.Iterator;

/**
 * @description: 队列是一种高级数据结构，是在链表基础上实现的一个数据结构。
 * 它是一种线性数据结构，只能在队列的一端插入数据，在另一端删除数据，这些操作由队列的队头与队尾完成。
 * 队列只支持在队列的一端（即队尾）插入数据，在另一端（即队头）删除数据。
 * 这些操作遵循两个基本原则：先进先出（FIFO）和后进先出（LIFO），可以分别称为队列和栈。
 * @author: wzh
 * @date: 2023/3/7 20:26
 */
class LinkedListQueue<E> implements Queue<E>, Iterable<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        public Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E> head = new Node<>(null, null);
    private Node<E> tail = head;

    public LinkedListQueue() {
        tail.next = head;
    }

    /**
     * 尾插
     *
     * @param value 待插入值
     * @return 插入成功返回 true, 插入失败返回 false
     */
    @Override
    public boolean offer(E value) {
        Node<E> added = new Node<>(value, head);
        tail.next = added;
        tail = added;
        return true;
    }

    /**
     * 从队列头获取值, 并移除
     *
     * @return 如果队列非空返回对头值, 否则返回 null
     */
    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        Node<E> first = head.next;
        head.next = first.next;
        if (first == tail) {
            tail = head;
        }
        return first.value;
    }

    /**
     * 从队列头获取值, 不移除
     *
     * @return 如果队列非空返回对头值, 否则返回 null
     */
    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return head.next.value;
    }

    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> p = head.next;

            @Override
            public boolean hasNext() {
                return p != head;
            }

            @Override
            public E next() {
                E value = p.value;
                p = p.next;
                return value;
            }
        };
    }
}
