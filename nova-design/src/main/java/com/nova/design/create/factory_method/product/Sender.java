package com.nova.design.create.factory_method.product;


/**
 * @description: 抽象行为 (把一段数据用Wi-Fi或者蓝牙发送出去)
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public interface Sender {

    void sendData(String data);

}
