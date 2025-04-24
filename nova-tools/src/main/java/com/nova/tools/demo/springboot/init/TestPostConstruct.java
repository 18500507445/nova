package com.nova.tools.demo.springboot.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: wzh
 * @description: PostConstruct
 * @date: 2023/10/08 17:32
 */
@Component
public class TestPostConstruct {

    public TestPostConstruct() {
        System.err.println("TestPostConstruct constructor");
    }

    /**
     * 如何在程序启动时 初始化数据或执行其它业务
     * 用来修饰一个非静态的void()方法，当bean创建完成时，会仅且执行一次作用：初始化数据
     * 例如：加载数据库中的数据字典缓存到redis中、用户可以对某一方法进行标注，来进行初始化某一个属性
     */
    @PostConstruct
    void init() {
        System.err.println("[@PostConstruct] 初始化");
    }
}
