package com.nova.design.action.memento;

/**
 * @description: 创建&存储状态
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class Originator {
    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public Memento saveStateToMemento() {
        return new Memento(this.state);
    }

    public void getStateFromMemento(Memento Memento) {
        this.state = Memento.getState();
    }
}