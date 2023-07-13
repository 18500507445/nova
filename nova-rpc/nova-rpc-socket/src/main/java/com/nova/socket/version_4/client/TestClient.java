package com.nova.socket.version_4.client;


import com.nova.rpc.manual.entity.BlogBO;
import com.nova.rpc.manual.entity.UserBO;
import com.nova.rpc.manual.service.BlogService;
import com.nova.rpc.manual.service.UserService;

public class TestClient {

    /**
     * 总结
     * 方式一：支持多种版本客户端的扩展（实现RPCClient接口）并且使用netty实现了客户端与服务端的通信
     * 方式二：自己定义的消息格式，使之支持多种消息类型，序列化方式，使用消息头加长度的方式解决粘包问题
     * 并且，实现了ObjectSerializer与JsonSerializer两种序列化器，也可以轻松扩展为其它序列化方式（实现Serialize接口）
     *
     * <p>
     * 此RPC最大痛点
     * 方式一：java自带序列化方式（Java序列化写入不仅是完整的类名，也包含整个类的定义，包含所有被引用的类），不够通用，不够高效
     * 方式二：服务端与客户端通信的host与port预先就必须知道的，每一个客户端都必须知道对应服务的ip与端口号， 并且如果服务挂了或者换地址了，就很麻烦。扩展性也不强
     */
    public static void main(String[] args) {

        // 构建一个使用java Socket/ netty/....传输的客户端
        RPCClient rpcClient = new NettyRPCClient("127.0.0.1", 8899);
        // 把这个客户端传入代理客户端
        RPCClientProxy rpcClientProxy = new RPCClientProxy(rpcClient);
        // 代理客户端根据不同的服务，获得一个代理类， 并且这个代理类的方法以或者增强（封装数据，发送请求）
        UserService userService = rpcClientProxy.getProxy(UserService.class);
        // 调用方法
        UserBO userByUserId = userService.getUserByUserId(10);
        System.out.println("从服务端得到的user为：" + userByUserId);

        UserBO user = UserBO.builder().userName("张三").id(100).sex(true).build();
        Integer integer = userService.insertUserId(user);
        System.out.println("向服务端插入数据：" + integer);

        BlogService blogService = rpcClientProxy.getProxy(BlogService.class);

        BlogBO blogById = blogService.getBlogById(10000);
        System.out.println("从服务端得到的blog为：" + blogById);

    }
}
