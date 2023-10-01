package com.nova.rpc.socket.version_3.client;


import com.nova.rpc.socket.entity.BlogBO;
import com.nova.rpc.socket.entity.UserBO;
import com.nova.rpc.socket.service.BlogService;
import com.nova.rpc.socket.service.UserService;
import com.nova.rpc.socket.version_2.client.ClientProxy;

public class RPCClient {

    /**
     * 总结：
     * 我们重构了服务端的代码，代码更加简洁，
     * 添加线程池版的服务端的实现，性能应该会有所提升（未测）
     * 并且服务端终于能够提供不同服务了， 功能更加完善，不再鸡肋
     * 此RPC最大的痛点
     * 传统的BIO与线程池网络传输性能低
     *
     */
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 8899);
        UserService userService = clientProxy.getProxy(UserService.class);

        UserBO userByUserId = userService.getUserByUserId(10);
        System.err.println("从服务端得到的user为：" + userByUserId);

        UserBO user = UserBO.builder().userName("张三").id(100).sex(true).build();
        Integer integer = userService.insertUserId(user);
        System.err.println("向服务端插入数据：" + integer);

        BlogService blogService = clientProxy.getProxy(BlogService.class);
        BlogBO blogById = blogService.getBlogById(10000);
        System.err.println("从服务端得到的blog为：" + blogById);
    }

}
