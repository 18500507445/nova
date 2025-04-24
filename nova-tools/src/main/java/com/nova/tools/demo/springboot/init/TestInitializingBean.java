package com.nova.tools.demo.springboot.init;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description: InitializingBean
 * 初始化bean的。InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，
 * 凡是继承该接口的类，在初始化bean的时候都会执行该方法。这个扩展点的触发时机在postProcessAfterInitialization之前
 * @date: 2023/10/08 17:34
 */
@Component
public class TestInitializingBean implements InitializingBean {

    /**
     * 使用场景：用户实现此接口，来进行系统启动的时候一些业务指标的初始化工作。
     */
    @Override
    public void afterPropertiesSet() {
        System.err.println("[InitializingBean] 初始化");
    }
}
