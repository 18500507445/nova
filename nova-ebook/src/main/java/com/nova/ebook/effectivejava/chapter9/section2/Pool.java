package com.nova.ebook.effectivejava.chapter9.section2;

import java.util.LinkedList;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/28 20:47
 */
class Pool {

    private final LinkedList<Integer> list;

    public Pool(LinkedList<Integer> list) {
        this.list = list;
    }

    public void add(Integer s) {
        synchronized (list) {
            list.add(s);
        }
    }

    public int size() {
        return list.size();
    }

}
