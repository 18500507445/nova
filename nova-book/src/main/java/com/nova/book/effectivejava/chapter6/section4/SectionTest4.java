package com.nova.book.effectivejava.chapter6.section4;

import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest4 {

    /**
     * 这个方法不是预期结果
     * remove(E e)和remove(int index)
     *
     * @param list
     * @param i
     */
    public void removeInvalid(List<Integer> list, int i) {
        if (list.contains(i)) {
            list.remove(i);
        }
    }

    /**
     * 下面方法实际一个回事，所以建议重载没问题
     *
     * @param i
     */
    public void dosh(Integer i) {

    }

    public void dosh(String s) {
        dosh(Integer.valueOf(s));
    }


    /**
     * 下来放个方法 比如第一个addUser对象，第二个addCat对象，建议不重载，直接定义两个不同的方法
     * @param o
     */
    public void add(Object o) {

    }

    public void add(Cat cat) {

    }
}
