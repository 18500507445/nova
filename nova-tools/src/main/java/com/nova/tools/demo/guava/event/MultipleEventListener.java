package com.nova.tools.demo.guava.event;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/10/13 19:55
 */
public class MultipleEventListener {

    @Subscribe
    public void listenerEventA(EventA eventA) {
        System.out.println("subscribe EventA:" + eventA.getMessage());
    }

    @Subscribe
    public void listenerEventB(EventB eventB) {
        System.out.println("subscribe EventB:" + eventB.getMessage());
    }

    /**
     * 现在假设我们把事件监听改一下，把listenerEventB的监听注释掉，加入一个DeadEvent的监听
     *
     * @param deadEvent
     */
    @Subscribe
    public void listenerDeadEvent(DeadEvent deadEvent) {
        System.out.println("deadEvent:" + deadEvent.getEvent());
    }
}
