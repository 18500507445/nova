package com.nova.rpc.socket.version_2.client;


import com.nova.rpc.socket.entity.RPCRequest;
import com.nova.rpc.socket.entity.RPCResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {

    /**
     * 传入参数Service接口的class对象，反射封装成一个request
     */
    private String host;

    private int port;

    /**
     * jdk 动态代理， 每一次代理对象调用方法，会经过此方法增强（反射获取request对象，socket发送至客户端）
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // request的构建，使用了lombok中的builder，代码简洁
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsTypes(method.getParameterTypes()).build();
        //数据传输
        RPCResponse response = sendRequest(host, port, request);
        return response.getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }

    /**
     * 这里负责底层与服务端的通信，发送的Request，接受的是Response对象
     * 客户端发起一次请求调用，Socket建立连接，发起请求Request，得到响应Response
     * 这里的request是封装好的（上层进行封装），不同的service需要进行不同的封装， 客户端只知道Service接口，需要一层动态代理根据反射封装不同的Service
     *
     * @param host
     * @param port
     * @param request
     * @return
     */
    public static RPCResponse sendRequest(String host, int port, RPCRequest request) {
        try {
            Socket socket = new Socket(host, port);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.err.println(request);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();

            return (RPCResponse) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println();
            return null;
        }
    }
}
