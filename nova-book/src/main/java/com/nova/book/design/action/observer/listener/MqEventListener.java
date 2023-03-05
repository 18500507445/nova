package com.nova.book.design.action.observer.listener;

/**
 * @description: mq 具体的观察者，当被通知时，可根据实际需要决定如何更新自己的状态；
 * @author: wzh
 * @date: 2022/12/31 10:08
 */
public class MqEventListener implements EventListener{

    @Override
    public void doEvent(Object obj) {
        System.out.println("Mq：" + obj);
    }
}
