package com.nova.book.design.create.abstract_factory;


import com.nova.book.design.create.factory_method.product.Sender;

/**
 * @description: 抽象工厂优势
 * 1.易于管理
 * 2.易于扩展
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public abstract class AbstractFactory {

    public abstract Sender createBluetoothSender();

    public abstract Sender createWifiSender();

}
