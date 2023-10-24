package com.nova.tools.product;

import com.nova.tools.product.AbstractChannelBase;
import com.nova.tools.product.ChannelEnum;
import com.nova.tools.product.ChannelFactory;
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

        jdChannel.functionA();
        String token = jdChannel.getToken();
        System.err.println("token = " + token);
        ChannelEnum channel = jdChannel.getChannel();
        System.err.println("channel = " + channel);
    }

}
