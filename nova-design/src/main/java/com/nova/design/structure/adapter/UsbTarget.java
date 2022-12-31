package com.nova.design.structure.adapter;

/**
 * 目标接口：客户所期待的接口
 *
 * @description: 可以是具体类 或 抽象的类，也可以是接口
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public interface UsbTarget {
    /**
     * 使用usb充电
     */
    void usb();
}
