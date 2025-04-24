package com.nova.tools.demo.springboot;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description: BeanFactoryPostProcessor
 * 这个接口是beanFactory的扩展接口，调用时机在spring在读取beanDefinition信息之后，实例化bean之前。
 * @date: 2023/10/08 17:18
 */
@Component
public class TestBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    /**
     * 用户可以通过实现这个扩展接口来自行处理一些东西，比如修改已经注册的beanDefinition的元信息
     */
    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.err.println("[BeanFactoryPostProcessor] 初始化");
    }
}
