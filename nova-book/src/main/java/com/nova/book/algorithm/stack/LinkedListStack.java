package com.nova.book.algorithm.stack;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

/**
 * @description: 单项列表实现栈
 * @author: wzh
 * @date: 2023/3/16 10:47
 */
class LinkedListStack<E> implements Stack<E>, Iterable<E> {

    private final int capacity;
    private int size;
    private final Node<E> head = new Node<>(null, null);

    public LinkedListStack(int capacity) {
        this.capacity = capacity;
    }

    /**
     * head -> 1 -> null
     *
     * @param value 待压入值 -> 2
     * @return
     */
    @Override
    public boolean push(E value) {
        if (isFull()) {
            return false;
        }
        //head指向新节点
        head.next = new Node<>(value, head.next);
        size++;
        return true;
    }

    /**
     * head -> 2 -> 1 -> null
     * 移除2
     *
     * @return
     */
    @Override
    public E pop() {
        if (isEmpty()) {
            return null;
        }
        Node<E> first = head.next;
        head.next = first.next;
        size--;
        return first.value;
    }

    @Override
    public E peek() {
        if (isFull()) {
            return null;
        }
        return head.next.value;
    }

    @Override
    public boolean isEmpty() {
        return head.next == null;
    }

    @Override
    public boolean isFull() {
        return size == capacity;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> p = head.next;

            @Override
            public boolean hasNext() {
                return p != null;
            }

            @Override
            public E next() {
                E value = p.value;
                p = p.next;
                return value;
            }
        };
    }

    static class Node<E> {
        E value;
        Node<E> next;

        public Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }
}

class LinkedListStackTest {

    /**
     * 压入测试
     */
    @Test
    public void demoA() {
        LinkedListStack<Object> stack = new LinkedListStack<>(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);

        System.err.println(JSONUtil.toJsonStr(stack));
    }

    /**
     * 移除顶部
     */
    @Test
    public void demoB(){
        LinkedListStack<Object> stack = new LinkedListStack<>(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.pop();
        System.err.println(JSONUtil.toJsonStr(stack));
    }


}
