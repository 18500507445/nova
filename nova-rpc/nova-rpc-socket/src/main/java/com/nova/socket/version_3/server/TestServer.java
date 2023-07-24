package com.nova.socket.version_3.server;


import com.nova.socket.service.BlogService;
import com.nova.socket.service.UserService;
import com.nova.socket.service.impl.BlogServiceImpl;
import com.nova.socket.service.impl.UserServiceImpl;

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