package com.nova.book.algorithm.stack;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

/**
 * @description: 数组实现
 * @author: wzh
 * @date: 2023/3/16 11:07
 */
class ArrayStack<E> implements Stack<E>, Iterable<E> {

    private final E[] array;

    /**
     * 栈顶指针
     */
    private int top = 0;

    /**
     * 0(底部) -> 1 -> 2(顶部)
     * top
     *
     * @param capacity
     */
    @SuppressWarnings("all")
    public ArrayStack(int capacity) {
        this.array = (E[]) new Object[capacity];
    }

    @Override
    public boolean push(E value) {
        if (isFull()) {
            return false;
        }
        array[top] = value;
        top++;
        return true;
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            return null;
        }
        E value = array[top - 1];
        top--;
        return value;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return array[top - 1];
    }

    @Override
    public boolean isEmpty() {
        return top == 0;
    }

    @Override
    public boolean isFull() {
        return top == array.length;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int p = top;

            @Override
            public boolean hasNext() {
                return p > 0;
            }

            @Override
            public E next() {
                return array[--p];
            }
        };
    }
}


class ArrayStackTest {

    /**
     * 压入测试
     */
    @Test
    public void demoA() {
        ArrayStack<Object> stack = new ArrayStack<>(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);

        System.out.println(JSONUtil.toJsonStr(stack));
    }

    /**
     * 移除顶部
     */
    @Test
    public void demoB() {
        ArrayStack<Object> stack = new ArrayStack<>(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.pop();
        System.out.println(JSONUtil.toJsonStr(stack));
    }


}