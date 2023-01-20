package com.nova.rpc.manual.version_5.client;


import com.nova.rpc.manual.entity.BlogBO;
import com.nova.rpc.manual.entity.UserBO;
import com.nova.rpc.manual.service.BlogService;
import com.nova.rpc.manual.service.UserService;
import com.nova.rpc.manual.version_4.client.RPCClient;
import com.nova.rpc.manual.version_4.client.RPCClientProxy;

public class TestClient {

    /**
     * 总结
     * 此版本中我们加入了注册中心，终于一个完整的RPC框架三个角色都有了：服务提供者，服务消费者，注册中心
     * <p>
     * 此版本最大痛点
     * 根据服务名查询地址时，我们返回的总是第一个IP，导致这个提供者压力巨大，而其它提供者调用不到
     */
    public static void main(String[] args) {

        // 构建一个使用java Socket/ netty/....传输的客户端
        RPCClient rpcClient = new NettyRPCClient();
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
