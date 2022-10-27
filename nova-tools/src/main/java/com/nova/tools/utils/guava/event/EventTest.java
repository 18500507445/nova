package com.nova.tools.utils.guava.event;

import com.google.common.eventbus.EventBus;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/10/13 19:55
 */
public class EventTest {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus("testMultiple");
        //注册监听
        eventBus.register(new MultipleEventListener());
        //发送不同类型的事件
        eventBus.post(new EventA("EventA Message"));
        eventBus.post(new EventA("EventA Message"));
        eventBus.post(new EventB("EventB Message"));
    }
}
