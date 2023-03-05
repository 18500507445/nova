package com.nova.book.design.action.memento;

import org.assertj.core.util.Lists;

import java.util.List;

/**
 * @description: 负责从 Memento 中恢复对象的状态
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class CareTaker {

    private final List<Memento> mementoList = Lists.newArrayList();

    public void add(Memento state) {
        this.mementoList.add(state);
    }

    public Memento get(int index) {
        return this.mementoList.get(index);
    }
}