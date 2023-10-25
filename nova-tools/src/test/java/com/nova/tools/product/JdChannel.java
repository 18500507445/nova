package com.nova.tools.product;

import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description 京东渠道子类
 * @date: 2023/10/24 10:34
 */
@Component
public class JdChannel extends AbstractChannelBase {

    @Override
    public ChannelEnum getChannel() {
        return ChannelEnum.JD;
    }

    @Override
    public String getToken() {
        return this.getChannel().getChannelName() + "_token";
    }

    @Override
    public void afterPropertiesSet() {
        ChannelFactory.add(getChannel(), this);
    }

    public void functionA() {
        System.out.println("京东渠道functionA");
    }


}
