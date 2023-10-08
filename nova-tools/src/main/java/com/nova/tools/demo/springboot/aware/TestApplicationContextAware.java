package com.nova.tools.demo.springboot.aware;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description ApplicationContextAware，可实现策略模式
 * @date: 2023/10/08 17:10
 */
@Component
public class TestApplicationContextAware implements ApplicationContextAware {
    /**
     *
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        System.err.println("[ApplicationContextAware] 初始化");
    }
}
