package com.nova.tools.demo.springboot;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description: DisposableBean
 * 扩展点也只有一个方法：destroy()，其触发时机为当此对象销毁时，会自动执行这个方法。
 * 比如说运行applicationContext.registerShutdownHook时，就会触发这个方法
 * @date: 2023/10/08 17:38
 */
@Component
public class TestDisposableBean implements DisposableBean {

    @Override
    public void destroy() {
        System.out.println("[DisposableBean] TestDisposableBean 初始化");
    }
}
