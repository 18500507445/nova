package com.nova.tools.demo.springboot.listener;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author: wzh
 * @description ApplicationListener
 * ApplicationListener可以监听某个事件的event，触发时机可以穿插在业务方法执行过程中，用户可以自定义某个业务事件
 * @date: 2023/10/08 17:39
 */
@Component
@RequiredArgsConstructor
public class TestApplicationListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Async
    @EventListener(condition = "#event.id == 1")
    public void eventOne(Event<List<String>> event) {
        List<String> list = event.getT();
        System.out.println("list = " + JSONObject.toJSONString(list));
    }

    @Async
    @EventListener(condition = "#event.id == 2")
    public void eventTwo(Event<Object> event) {
        Object t = event.getT();
        System.out.println("t = " + JSONObject.toJSONString(t));
    }

    @Test
    public void testPushEvent() {
        applicationEventPublisher.publishEvent(new Event<>(1, Arrays.asList("123", "456")));
        applicationEventPublisher.publishEvent(new Event<>(2, "123"));
    }

}
