package com.nova.rpc.manual.version_6.client;


import com.nova.rpc.manual.entity.BlogBO;
import com.nova.rpc.manual.entity.UserBO;
import com.nova.rpc.manual.service.BlogService;
import com.nova.rpc.manual.service.UserService;
import com.nova.rpc.manual.version_4.client.RPCClient;
import com.nova.rpc.manual.version_4.client.RPCClientProxy;

public class TestClient {

    /**
     * 总结
     * 这一版本中，我们实现负载均衡的两种策略：随机与轮询。
     * 在这一版本下，一个完整功能的RPC已经出现，当然还有许多性能上与代码质量上的工作需要完成。
     *
     * 此版本最大痛点
     * 客户端每次发起请求都要先与zookeeper进行通信得到地址，效率低下。
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

        // 测试json调用空参数方法
        System.out.println(userService.hello());

    }
}
