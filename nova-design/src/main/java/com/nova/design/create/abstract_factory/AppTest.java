package com.nova.design.create.abstract_factory;


import com.nova.design.create.factory_method.product.Sender;
import org.junit.jupiter.api.Test;

/**
 * @description: 抽象工厂测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    /**
     * 在实际应用中最为广泛
     */
    @Test
    public void abstractTest() {
        SenderFactory senderFactory = new SenderFactory();
        Sender sender = senderFactory.createBluetoothSender();
        sender.sendData("消息");
    }
}