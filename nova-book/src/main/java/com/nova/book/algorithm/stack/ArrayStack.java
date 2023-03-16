package com.nova.book.algorithm.stack;

import java.util.Iterator;

/**
 * @description: 数组实现
 * @author: wzh
 * @date: 2023/3/16 11:07
 */
class ArrayStack<E> implements Stack<E>, Iterable<E>{

    @Override
    public boolean push(E value) {
        return false;
    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
