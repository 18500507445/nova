package com.nova.socket.version_6.client;

import com.nova.socket.codec.JsonSerializer;
import com.nova.socket.codec.MyDecode;
import com.nova.socket.codec.MyEncode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * 同样的与服务端解码和编码格式
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //方式二 自定义fastjson
        pipeline.addLast(new MyDecode());
        pipeline.addLast(new MyEncode(new JsonSerializer()));

        pipeline.addLast(new NettyClientHandler());
    }
}
