package com.nova.book.design.action.state;

/**
 * @description: 状态接口
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public interface State {

    void doAction(Context context);

}

class StartState implements State {

    @Override
    public void doAction(Context context) {
        System.out.println("Player is in start state");
        context.setState(this);
    }

    @Override
    public String toString() {
        return "Start State";
    }
}

class StopState implements State {

    @Override
    public void doAction(Context context) {
        System.out.println("Player is in stop state");
        context.setState(this);
    }

    @Override
    public String toString() {
        return "Stop State";
    }
}
