package com.nova.socket.version_4.server;

import com.nova.socket.codec.JsonSerializer;
import com.nova.socket.codec.MyDecode;
import com.nova.socket.codec.MyEncode;
import com.nova.socket.version_3.server.ServiceProvider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;

/**
 * 初始化，主要负责序列化的编码解码， 需要解决netty的粘包问题
 */
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 方式一 这里使用的还是java 序列化方式， netty的自带的解码编码支持传输这种结构
        //pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        //pipeline.addLast(new LengthFieldPrepender(4));
        //pipeline.addLast(new ObjectEncoder());
        //pipeline.addLast(new ObjectDecoder(new ClassResolver() {
        //    @Override
        //    public Class<?> resolve(String className) throws ClassNotFoundException {
        //        return Class.forName(className);
        //    }
        //}));

        //方式二
        pipeline.addLast(new MyDecode());
        pipeline.addLast(new MyEncode(new JsonSerializer()));

        pipeline.addLast(new NettyServerHandler(serviceProvider));
    }
}
