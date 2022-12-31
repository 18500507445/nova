package com.nova.spring.aop;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @description: 使用接口实现AOP
 * @author: wzh
 * @date: 2022/12/29 16:12
 */
public class AopAdvice implements MethodBeforeAdvice, AfterReturningAdvice {

    /**
     * 前面我们介绍了如何使用xml配置一个AOP操作，这节课我们来看看如何使用Advice实现AOP。
     * <p>
     * 它与我们之前学习的动态代理更接近一些，比如在方法开始执行之前或是执行之后会去调用我们实现的接口，
     * 首先我们需要将一个类实现Advice接口，只有实现此接口，才可以被通知，
     * 比如我们这里使用MethodBeforeAdvice表示是一个在方法执行之前的动作：
     *
     * @param method
     * @param args
     * @param target
     * @throws Throwable
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("我是方法执行之前！");
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("我是方法执行之后！");
    }
}
