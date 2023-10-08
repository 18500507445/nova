package com.nova.tools.demo.springboot.listener;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description ApplicationListener
 * ApplicationListener可以监听某个事件的event，触发时机可以穿插在业务方法执行过程中，用户可以自定义某个业务事件
 * @date: 2023/10/08 17:39
 */
@Component
public class TestApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static boolean aFlag = false;

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        if (!aFlag) {
            aFlag = true;
            System.err.println("[ApplicationListener] 初始化，我已经监听到了");
        }
    }
}
