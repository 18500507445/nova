package com.nova.socket.version_5.server;


import com.nova.socket.service.BlogService;
import com.nova.socket.service.UserService;
import com.nova.socket.service.impl.BlogServiceImpl;
import com.nova.socket.service.impl.UserServiceImpl;
import com.nova.socket.version_3.server.RPCServer;

public class TestServer {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();
        // 这里重用了服务暴露类，顺便在注册中心注册，实际上应分开，每个类做各自独立的事
        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 8899);
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);
        RPCServer server = new NettyServer(serviceProvider);
        server.start(8899);
    }
}