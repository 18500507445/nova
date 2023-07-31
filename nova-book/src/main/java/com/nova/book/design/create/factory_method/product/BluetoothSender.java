package com.nova.book.design.create.factory_method.product;


/**
 * @description: 具体实现 蓝牙发送
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class BluetoothSender implements Sender {

    @Override
    public void sendData(String data) {
        System.err.println("Send data by bluetooth,data=" + data);
    }
}
