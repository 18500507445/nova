package com.nova.design.action.observer.listener;

/**
 * @description: 观察者，提供更新自己状态的行为
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public interface EventListener {

    void doEvent(Object obj);

}