package com.nova.book.design.create.abstract_factory;


import com.nova.book.design.create.factory_method.product.BluetoothSender;
import com.nova.book.design.create.factory_method.product.Sender;
import com.nova.book.design.create.factory_method.product.WifiSender;

/**
 * @description: 抽象工厂优势
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class SenderFactory extends AbstractFactory {

    @Override
    public Sender createBluetoothSender() {
        return new BluetoothSender();
    }

    @Override
    public Sender createWifiSender() {
        return new WifiSender();
    }
}
