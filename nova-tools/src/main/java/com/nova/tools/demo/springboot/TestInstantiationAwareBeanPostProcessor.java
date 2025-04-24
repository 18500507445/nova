package com.nova.tools.demo.springboot;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.beans.PropertyDescriptor;

/**
 * @author: wzh
 * @description: InstantiationAwareBeanPostProcessor
 * 接口继承了BeanPostProcess接口，BeanPostProcess接口只在bean的初始化阶段进行扩展（注入spring上下文前后），
 * 而InstantiationAwareBeanPostProcessor接口在此基础上增加了3个方法，把可扩展的范围增加了实例化阶段和属性注入阶段。
 * @date: 2023/10/08 17:22
 */
//@Component
public class TestInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    /**
     * 使用场景：这个扩展点非常有用 ，无论是写中间件和业务中，都能利用这个特性。
     * 比如对实现了某一类接口的bean在各个生命期间进行收集，或者对某个类型的bean进行统一的设值等等。
     * </p>
     * 实例化bean之前，相当于new这个bean之前
     */
    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        System.err.println("[TestInstantiationAwareBeanPostProcessor] before initialization " + beanName);
        return bean;
    }

    /**
     * 实例化bean之后，相当于new这个bean之后
     */
    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        System.err.println("[TestInstantiationAwareBeanPostProcessor] after initialization " + beanName);
        return bean;
    }

    /**
     * bean已经实例化完成，在属性注入时阶段触发，@Autowired,@Resource等注解原理基于此方法实现
     */
    @Override
    public Object postProcessBeforeInstantiation(@NotNull Class<?> beanClass, @NotNull String beanName) throws BeansException {
        System.err.println("[TestInstantiationAwareBeanPostProcessor] before instantiation " + beanName);
        return null;
    }

    /**
     * 初始化bean之前，相当于把bean注入spring上下文之前
     */
    @Override
    public boolean postProcessAfterInstantiation(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        System.err.println("[TestInstantiationAwareBeanPostProcessor] after instantiation " + beanName);
        return true;
    }

    /**
     * 初始化bean之后，相当于把bean注入spring上下文之后
     */
    @Override
    public PropertyValues postProcessPropertyValues(@NotNull PropertyValues pvs, @NotNull PropertyDescriptor[] pds,
                                                    @NotNull Object bean, @NotNull String beanName) throws BeansException {
        System.err.println("[TestInstantiationAwareBeanPostProcessor] postProcessPropertyValues " + beanName);
        return pvs;
    }
}
