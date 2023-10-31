package com.nova.tools.product;

import com.nova.tools.product.base.AbstractChannelBase;
import com.nova.tools.product.base.ChannelFactory;
import com.nova.tools.product.channel.YxChannel;
import com.nova.tools.product.enums.ChannelEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description 渠道测试类
 * @date: 2023/10/24 10:46
 */
@SpringBootTest
public class ChannelTest {

    @Resource
    private ChannelFactory channelFactory;

    @Test
    public void channelTest() {
        AbstractChannelBase jdChannel = channelFactory.get(ChannelEnum.JD);
        System.err.println("JdToken = " + jdChannel.getToken());
        System.err.println("JdRefreshToken = " + jdChannel.refreshToken());
        System.err.println("JdChannel = " + jdChannel.getChannel());

        AbstractChannelBase yxChannel = channelFactory.get(ChannelEnum.YX);
        System.err.println("YxRefreshToken = " + yxChannel.refreshToken());
    }

    @Test
    public void messageTest() {
        YxChannel yxChannel = channelFactory.get(ChannelEnum.JD, YxChannel.class);
        System.err.println("productChange = " + yxChannel.productChange(null));
    }

}
