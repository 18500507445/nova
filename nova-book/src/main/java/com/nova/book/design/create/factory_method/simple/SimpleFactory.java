package com.nova.book.design.create.factory_method.simple;


import com.nova.book.design.create.factory_method.product.BluetoothSender;
import com.nova.book.design.create.factory_method.product.Sender;
import com.nova.book.design.create.factory_method.product.WifiSender;

/**
 * @description: 简单工厂
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class SimpleFactory {

    public static Sender createSender(String mode) {
        switch (mode) {
            case "wifi":
                return new WifiSender();
            case "bluetooth":
                return new BluetoothSender();
            default:
                throw new IllegalArgumentException("illegal type: " + mode);
        }
    }

}
