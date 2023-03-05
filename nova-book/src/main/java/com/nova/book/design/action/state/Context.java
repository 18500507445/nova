package com.nova.book.design.action.state;


/**
 * @description: 带有某个状态的类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class Context {

    private State state;

    public Context() {
        this.state = null;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }
}