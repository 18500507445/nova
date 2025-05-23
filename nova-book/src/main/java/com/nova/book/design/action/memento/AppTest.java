package com.nova.book.design.action.memento;


import org.junit.jupiter.api.Test;

/**
 * @description: 备忘录模式测试类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        Originator originator = new Originator();
        originator.setState("#1");

        CareTaker careTaker = new CareTaker();
        careTaker.add(originator.saveStateToMemento());

        originator.setState("#2");
        careTaker.add(originator.saveStateToMemento());

        originator.setState("#3");
        System.err.println("当前: " + originator.getState());

        originator.getStateFromMemento(careTaker.get(0));
        System.err.println("第1个保存的状态: " + originator.getState());
        originator.getStateFromMemento(careTaker.get(1));
        System.err.println("第2个保存的状态: " + originator.getState());
    }

}