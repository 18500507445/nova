package com.nova.tools.demo.springboot;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description ApplicationContextInitializer，这是整个spring容器在刷新之前初始化ConfigurableApplicationContext的回调接口，
 * 简单来说，就是在容器刷新之前调用此类的initialize方法。这个点允许被用户自己扩展。用户可以在整个spring容器还没被初始化之前做一些事情
 * @date: 2023/10/08 17:07
 */
@Component
public class TestApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * 应用场景：在最开始激活一些配置，或者利用这时候class还没被类加载器加载的时机，进行动态字节码注入等操作
     * <p>
     * 因为这时候spring容器还没被初始化，所以想要自己的扩展的生效
     * 方式一：启动类，springApplication.addInitializers(new TestApplicationContextInitializer())
     * 方式二：自动装配SPI扩展机制，org.springframework.context.ApplicationContextInitializer=com.example.demo.TestApplicationContextInitializer
     * 方式三：配置文件配置，context.initializer.classes=com.example.demo.TestApplicationContextInitializer
     *
     * @param applicationContext
     */
    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
        System.err.println("[ApplicationContextInitializer] 初始化");
    }
}
