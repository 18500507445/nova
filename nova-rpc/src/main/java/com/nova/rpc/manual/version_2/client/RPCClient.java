package com.nova.rpc.manual.version_2.client;


import com.nova.rpc.manual.entity.UserBO;
import com.nova.rpc.manual.service.UserService;

public class RPCClient {

    /**
     * 总结
     * 定义更加通用的消息格式：Request 与Response格式， 从此可能调用不同的方法，与返回各种类型的数据。
     * 使用了动态代理进行不同服务方法的Request的封装，
     * 客户端更加松耦合，不再与特定的Service，host，port绑定
     *
     * 存在的痛点
     * 服务端我们直接绑定的是UserService服务，如果还有其它服务接口暴露呢?（多个服务的注册）
     * 服务端以BIO的方式性能是否太低，
     * 服务端功能太复杂：监听，处理。需要松耦合
     *
     * @param args
     */
    public static void main(String[] args) {

        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 8899);
        UserService proxy = clientProxy.getProxy(UserService.class);

        // 服务的方法1
        UserBO userByUserId = proxy.getUserByUserId(10);
        System.out.println("从服务端得到的user为：" + userByUserId);
        // 服务的方法2
        UserBO user = UserBO.builder().userName("张三").id(100).sex(true).build();
        Integer integer = proxy.insertUserId(user);
        System.out.println("向服务端插入数据：" + integer);
    }

}
