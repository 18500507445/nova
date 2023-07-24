package com.nova.socket.version_4.server;


import com.nova.socket.service.BlogService;
import com.nova.socket.service.UserService;
import com.nova.socket.service.impl.BlogServiceImpl;
import com.nova.socket.service.impl.UserServiceImpl;
import com.nova.socket.version_3.server.RPCServer;
import com.nova.socket.version_3.server.ServiceProvider;

public class TestServer {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);

        RPCServer server = new NettyServer(serviceProvider);
        server.start(8899);
    }
}