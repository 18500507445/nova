package com.nova.design.create.factory_method.product;


/**
 * @description: 具体实现 wifi发送
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class WifiSender implements Sender {

    @Override
    public void sendData(String data) {
        System.out.println("Send data by wifi,data=" + data);
    }
}
