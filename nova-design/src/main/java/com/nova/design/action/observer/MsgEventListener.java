package com.nova.design.action.observer;

/**
 * @description: 短信
 * @author: wzh
 * @date: 2022/12/31 10:08
 */
public class MsgEventListener implements EventListener {

    @Override
    public void doEvent(Object obj) {
        System.out.println("短信：" + obj);
    }
}
