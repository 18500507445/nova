package com.nova.book.design.action.iterator;

import java.util.Iterator;

/**
 * @description: 设计一个简单的数组集合，迭代此集合内的元素
 * @author: wzh
 * @date: 2023/4/8 23:14
 */
class ArrayCollection<T> implements Iterable {

    /**
     * 底层使用一个数组存放数据
     */
    private final T[] array;

    public ArrayCollection(T[] array) {
        this.array = array;
    }

    /**
     * 静态方法直接把数组转成ArrayCollection，和new一个效果
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> ArrayCollection<T> of(T[] array) {
        return new ArrayCollection<>(array);
    }

    @Override
    public Iterator iterator() {
        return new ArrayIterator();
    }

    public class ArrayIterator implements Iterator<T> {

        /**
         * 当前位置
         */
        private int cur = 0;

        /**
         * 判断长度，是否还有下一个元素
         *
         * @return
         */
        @Override
        public boolean hasNext() {
            return cur < array.length;
        }

        /**
         * 返回当前指针位置的元素，然后向后移动一位
         *
         * @return
         */
        @Override
        public T next() {
            return array[cur++];
        }
    }
}
