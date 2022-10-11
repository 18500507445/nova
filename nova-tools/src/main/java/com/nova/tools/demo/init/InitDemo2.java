package com.nova.tools.demo.init;

import org.springframework.beans.factory.InitializingBean;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/10/11 10:39
 */
public class InitDemo2 implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        System.out.println("[实现InitializingBean] 初始化");
    }
}
