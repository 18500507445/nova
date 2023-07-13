package com.nova.socket.version_4.server;


import com.nova.rpc.manual.service.BlogService;
import com.nova.rpc.manual.service.UserService;
import com.nova.rpc.manual.service.impl.BlogServiceImpl;
import com.nova.rpc.manual.service.impl.UserServiceImpl;
import com.nova.rpc.manual.version_3.server.RPCServer;
import com.nova.rpc.manual.version_3.server.ServiceProvider;

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