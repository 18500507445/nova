package com.nova.socket.version_1.server;


import com.nova.socket.entity.UserBO;
import com.nova.socket.service.impl.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 服务端以BIO的方式监听Socket，如有数据，调用对应服务的实现类执行任务，将结果返回给客户端
 * @author: wzh
 * @date: 2023/1/19 19:48
 */
public class RPCServer {

    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        try {
            ServerSocket serverSocket = new ServerSocket(8899);
            System.err.println("服务端启动了");
            // BIO的方式监听Socket
            while (true) {
                Socket socket = serverSocket.accept();
                // 开启一个线程去处理
                new Thread(() -> {
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        // 读取客户端传过来的id
                        Integer id = ois.readInt();
                        UserBO userByUserId = userService.getUserByUserId(id);
                        // 写入User对象给客户端
                        oos.writeObject(userByUserId);
                        oos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("从IO中读取数据错误");
                    }
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("服务器启动失败");
        }
    }
}
