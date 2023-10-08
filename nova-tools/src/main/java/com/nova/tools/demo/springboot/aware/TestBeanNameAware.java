package com.nova.tools.demo.springboot.aware;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description BeanNameAware
 * 这个类也是Aware扩展的一种，触发点在bean的初始化之前，也就是postProcessBeforeInitialization之前，这个类的触发点方法只有一个：setBeanName
 * @date: 2023/10/08 17:28
 */
@Component
public class TestBeanNameAware implements BeanNameAware {

    public TestBeanNameAware() {
        System.err.println("TestBeanNameAware constructor 初始化");
    }

    /**
     * 使用场景：用户可以扩展这个点，在初始化bean之前拿到spring容器中注册的的beanName，来自行修改这个beanName的值。
     */
    @Override
    public void setBeanName(@NotNull String name) {
        System.err.println("[BeanNameAware] 初始化" + name);
    }

}
