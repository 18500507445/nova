package com.nova.tools.demo.init;

import org.springframework.beans.factory.InitializingBean;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/11 10:39
 */
class InitDemo2 implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        System.out.println("[实现InitializingBean] 初始化");
    }
}
