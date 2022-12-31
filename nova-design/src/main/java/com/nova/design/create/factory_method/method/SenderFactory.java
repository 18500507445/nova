package com.nova.design.create.factory_method.method;


import com.nova.design.create.factory_method.product.Sender;

/**
 * @description: 在简单工厂模式的基础上，让我们对工厂类也升级一下，首先定义一个工厂类接口：
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public interface SenderFactory {

    Sender createSender();

}
