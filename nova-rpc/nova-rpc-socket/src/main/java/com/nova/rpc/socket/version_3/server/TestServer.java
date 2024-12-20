package com.nova.rpc.socket.version_3.server;


import com.nova.rpc.socket.service.BlogService;
import com.nova.rpc.socket.service.UserService;
import com.nova.rpc.socket.service.impl.BlogServiceImpl;
import com.nova.rpc.socket.service.impl.UserServiceImpl;

public class TestServer {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);

        RPCServer rPCServer = new ThreadPoolRPCRPCServer(serviceProvider);
        rPCServer.start(8899);
    }
}