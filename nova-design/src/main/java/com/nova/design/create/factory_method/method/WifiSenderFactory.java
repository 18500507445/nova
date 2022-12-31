package com.nova.design.create.factory_method.method;


import com.nova.design.create.factory_method.product.Sender;
import com.nova.design.create.factory_method.product.WifiSender;

/**
 * @description: 为每一个发送器的实现类各创建一个具体的工厂方法去实现这个接口
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class WifiSenderFactory implements SenderFactory {

    @Override
    public Sender createSender() {
        return new WifiSender();
    }
}
