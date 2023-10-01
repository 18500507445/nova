package com.nova.rpc.socket.version_1.client;


import com.nova.rpc.socket.entity.UserBO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * @description: 客户端建立Socket连接，传输Id给服务端，得到返回的UserBO对象
 * @author: wzh
 * @date: 2023/1/19 19:48
 */
public class RPCClient {

    /**
     * 此RPC的最大痛点：
     * 只能调用服务端Service唯一确定的方法，如果有两个方法需要调用呢?（Request需要抽象）
     * 返回值只支持User对象，如果需要传一个字符串或者一个Dog，String对象呢（Response需要抽象）
     * 客户端不够通用，host，port， 与调用的方法都特定（需要抽象）
     */
    public static void main(String[] args) {
        try {
            // 建立Socket连接
            Socket socket = new Socket("127.0.0.1", 8899);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 传给服务器id
            objectOutputStream.writeInt(new Random().nextInt());
            objectOutputStream.flush();
            // 服务器查询数据，返回对应的对象
            UserBO user = (UserBO) objectInputStream.readObject();
            System.err.println("服务端返回的User:" + user);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("客户端启动失败");
        }
    }
}
