package com.nova.tools.demo.springboot;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description BeanDefinitionRegistryPostProcessor，这个接口在读取项目中的beanDefinition之后执行，提供一个补充的扩展点
 * @date: 2023/10/08 17:11
 */
@Component
public class TestBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * 使用场景：动态注册自己的beanDefinition，可以加载classpath之外的bean
     */
    @Override
    public void postProcessBeanDefinitionRegistry(@NotNull BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        System.err.println("[BeanDefinitionRegistryPostProcessor] postProcessBeanDefinitionRegistry 初始化");
    }

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.err.println("[BeanDefinitionRegistryPostProcessor] postProcessBeanFactory 初始化");
    }
}
