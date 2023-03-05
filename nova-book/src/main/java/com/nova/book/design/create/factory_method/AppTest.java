package com.nova.book.design.create.factory_method;


import com.nova.book.design.create.factory_method.method.BluetoothSenderFactory;
import com.nova.book.design.create.factory_method.method.SenderFactory;
import com.nova.book.design.create.factory_method.method.WifiSenderFactory;
import com.nova.book.design.create.factory_method.product.Sender;
import com.nova.book.design.create.factory_method.simple.SimpleFactory;
import org.junit.jupiter.api.Test;

/**
 * @description: 工厂方法测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {


    /**
     * 简单工厂
     */
    @Test
    public void simpleTest() {
        Sender sender = SimpleFactory.createSender("wifi");
        sender.sendData("发送消息");
    }

    /**
     * 方法工厂
     */
    @Test
    public void methodTest() {
        SenderFactory factory;
        String mode = "wifi";
        if ("wifi".equals(mode)) {
            factory = new WifiSenderFactory();
        } else {
            factory = new BluetoothSenderFactory();
        }
        Sender sender = factory.createSender();
        sender.sendData("发送消息");
    }
}