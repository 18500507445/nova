package com.nova.book.effectivejava.chapter9.section2;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @description: 并发集合
 * @author: wzh
 * @date: 2023/2/28 21:07
 */
public class CopyPool {

    /**
     * 适用于多读，少写的场景，性能很好
     * 弱一致性，也就是稍微有点延迟
     */
    private final CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();

    public CopyPool() {

    }

    public void add(Integer s) {
        list.add(s);
    }

    public int size() {
        return list.size();
    }
}
