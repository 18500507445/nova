package com.nova.design.action.observer;

import java.util.Observable;

/**
 * @description:
 * @author: wzh
 * @date: 2022/12/31 10:16
 */
public class MyObservable extends Observable {

    /**
     * 子类提升方法从protected 到 public
     * Observable设置为protected说明JDK不希望用户直接使用这个方法
     */
    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }
}
