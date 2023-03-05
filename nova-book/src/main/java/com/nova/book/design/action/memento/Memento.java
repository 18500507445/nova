package com.nova.book.design.action.memento;

/**
 * @description: 包含了要被恢复的对象的状态
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class Memento {

    private final String state;

    public Memento(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}