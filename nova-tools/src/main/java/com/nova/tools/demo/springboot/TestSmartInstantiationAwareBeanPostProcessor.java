package com.nova.tools.demo.springboot;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

import java.lang.reflect.Constructor;

/**
 * @author: wzh
 * @description: SmartInstantiationAwareBeanPostProcessor
 * @date: 2023/10/08 17:25
 */
//@Component
public class TestSmartInstantiationAwareBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {

    /**
     * 该触发点发生在postProcessBeforeInstantiation之前(在图上并没有标明，因为一般不太需要扩展这个点)，
     * 这个方法用于预测Bean的类型，返回第一个预测成功的Class类型，如果不能预测返回null；
     * 当你调用BeanFactory.getType(name)时当通过bean的名字无法得到bean类型信息时就调用该回调方法来决定类型信息。
     */
    @Override
    public Class<?> predictBeanType(@NotNull Class<?> beanClass, @NotNull String beanName) throws BeansException {
        System.err.println("[TestSmartInstantiationAwareBeanPostProcessor] predictBeanType " + beanName);
        return beanClass;
    }

    /**
     * 该触发点发生在postProcessBeforeInstantiation之后，用于确定该bean的构造函数之用，
     * 返回的是该bean的所有构造函数列表。用户可以扩展这个点，来自定义选择相应的构造器来实例化这个bean
     */
    @Override
    public Constructor<?>[] determineCandidateConstructors(@NotNull Class<?> beanClass, @NotNull String beanName) throws BeansException {
        System.err.println("[TestSmartInstantiationAwareBeanPostProcessor] determineCandidateConstructors " + beanName);
        return null;
    }

    /**
     * 该触发点发生在postProcessAfterInstantiation之后，当有循环依赖的场景，当bean实例化好之后，
     * 为了防止有循环依赖，会提前暴露回调方法，用于bean实例化的后置处理。这个方法就是在提前暴露的回调方法中触发
     */
    @Override
    public Object getEarlyBeanReference(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        System.err.println("[TestSmartInstantiationAwareBeanPostProcessor] getEarlyBeanReference " + beanName);
        return bean;
    }

}
