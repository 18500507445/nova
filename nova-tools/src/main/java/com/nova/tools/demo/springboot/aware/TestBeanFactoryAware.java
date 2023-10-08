package com.nova.tools.demo.springboot.aware;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description BeanFactoryAware
 * 发生在bean的实例化之后，注入属性之前，也就是Setter之前。这个类的扩展点方法为setBeanFactory，可以拿到BeanFactory这个属性
 * @date: 2023/10/08 17:27
 */
@Component
public class TestBeanFactoryAware implements BeanFactoryAware {

    /**
     * 应用场景：可以在bean实例化之后，但还未初始化之前，
     * 拿到BeanFactory，在这个时候，可以对每个bean作特殊化的定制。也或者可以把BeanFactory拿到进行缓存，日后使用
     */
    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        System.err.println("[TestBeanFactoryAware] 初始化");
    }

}
