package com.nova.tools.product;

import cn.hutool.json.JSONUtil;
import com.nova.tools.product.base.AbstractChannelBase;
import com.nova.tools.product.base.ChannelFactory;
import com.nova.tools.product.channel.YxChannel;
import com.nova.tools.product.enums.ChannelEnum;
import com.nova.tools.product.init.CommonInit;
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

    @Resource
    private CommonInit commonInit;

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
        YxChannel yxChannel = channelFactory.get(ChannelEnum.YX, YxChannel.class);
        System.err.println("productChange = " + yxChannel.productChange(null));
    }


    @Test
    public void configTest() {
        YxChannel yxChannel = channelFactory.get(ChannelEnum.YX, YxChannel.class);
        System.out.println("config = " + JSONUtil.toJsonStr(yxChannel.getConfig()));
        commonInit.refresh();
    }

}