package com.nova.tools.product;

import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description 严选渠道子类
 * @date: 2023/10/24 10:39
 */
@Component
public class YxChannel extends AbstractChannelBase {

    @Override
    public ChannelEnum getChannel() {
        return ChannelEnum.YX;
    }

    @Override
    public String getToken() {
        return this.getChannel().getChannelName() + "_token";
    }

    @Override
    public void afterPropertiesSet() {
        ChannelFactory.add(getChannel(), this);
    }

    public void functionB() {
        System.out.println("严选渠道functionB");
    }
}
