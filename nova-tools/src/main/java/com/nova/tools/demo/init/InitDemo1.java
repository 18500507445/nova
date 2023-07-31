package com.nova.tools.demo.init;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/11 10:35
 */
class InitDemo1 {

    /**
     * 如何在程序启动时 初始化数据或执行其它业务
     *
     * @PostConstruct： 用来修饰一个非静态的void()方法，当bean创建完成时，会仅且执行一次 作用：初始化数据
     * 例如：加载数据库中的数据字典缓存到redis中
     */
    @PostConstruct
    void init() {
        System.err.println("[注解@PostConstruct] 初始化");
    }
}
