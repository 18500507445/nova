package com.nova.book.design.action.observer;


import com.nova.book.design.action.observer.listener.EventListener;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * @description: 可被观察的对象接口，提供注册、注销、通知所有的观察者的行为
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class EventManager {

    private final List<EventListener> listenerList = Lists.newArrayList();

    /**
     * 订阅
     *
     * @param listener 监听
     */
    public void subscribe(EventListener listener) {
        this.listenerList.add(listener);
    }

    /**
     * 取消订阅
     *
     * @param listener 监听
     */
    public void unsubscribe(EventListener listener) {
        this.listenerList.remove(listener);
    }

    /**
     * 通知
     *
     * @param obj 内容
     */
    public void notify(Object obj) {
        for (EventListener listener : this.listenerList) {
            listener.doEvent(obj);
        }
    }

}
