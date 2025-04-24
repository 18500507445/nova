package com.nova.tools.product;

import cn.hutool.json.JSONUtil;
import com.nova.tools.product.base.ChannelFactory;
import com.nova.tools.product.base.ChannelInit;
import com.nova.tools.product.base.impl.YxChannel;
import com.nova.tools.product.enums.ChannelEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description: 渠道测试类
 * @date: 2023/10/24 10:46
 */
@SpringBootTest
public class ChannelTest {

    @Resource
    private ChannelFactory channelFactory;

    @Resource
    private ChannelInit channelInit;

    @Test
    public void messageTest() {
        YxChannel yxChannel = channelFactory.get(ChannelEnum.YX, YxChannel.class);
        assert yxChannel != null;
        yxChannel.productMessage("");
    }

    @Test
    public void channelTest() {
        YxChannel yxChannel = channelFactory.get(ChannelEnum.YX, YxChannel.class);
        System.out.println("config = " + JSONUtil.toJsonStr(yxChannel.getConfig()));
        channelInit.refresh();
    }

}
