package com.nova.tools.product;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author: wzh
 * @description 渠道抽象基类，实现Spring的InitializingBean，各个渠道子类重写方法放入容器
 * 设计思路，各个渠道都有的，抽取成抽象方法，子类必须实现，如果拿不准的，抽取成普通方法，求同存异
 * @date: 2023/10/24 10:25
 */
public abstract class AbstractChannelBase implements InitializingBean {

    /**
     * 获取当前渠道
     */
    public abstract ChannelEnum getChannel();

    /**
     * 获取渠道token
     */
    public abstract String getToken();

    /**
     * 如果子类没有复写次方法，刨除异常
     */
    public void functionA() {
        throw new UnsupportedOperationException("子类没有此方法，如需请手动复写");
    }

    public void functionB() {
        throw new UnsupportedOperationException("子类没有此方法，如需请手动复写");
    }
}
