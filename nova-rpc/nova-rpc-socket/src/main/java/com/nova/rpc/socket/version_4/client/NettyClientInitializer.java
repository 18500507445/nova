package com.nova.rpc.socket.version_4.client;

import com.nova.rpc.socket.codec.JsonSerializer;
import com.nova.rpc.socket.codec.MyDecode;
import com.nova.rpc.socket.codec.MyEncode;
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

        //方式一 消息格式 [长度][消息体]、计算当前待发送消息的长度，写入到前4个字节中
        //pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        //pipeline.addLast(new LengthFieldPrepender(4));
        //pipeline.addLast(new ObjectEncoder());
        //pipeline.addLast(new ObjectDecoder(new ClassResolver() {
        //    @Override
        //    public Class<?> resolve(String className) throws ClassNotFoundException {
        //        return Class.forName(className);
        //    }
        //}));

        //方式二 自定义fastjson
        pipeline.addLast(new MyDecode());
        pipeline.addLast(new MyEncode(new JsonSerializer()));

        pipeline.addLast(new NettyClientHandler());
    }
}
