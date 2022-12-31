package com.nova.design.create.singleton.lazy;

/**
 * @description: 双重检索模式
 * @author: wangzehui
 * @date: 2022/12/31 14:30
 */
public class DoubleRetrieval {

    /**
     * 私有实例 volatile
     */
    private volatile static DoubleRetrieval instance;

    /**
     * 私有构造方法
     */
    private DoubleRetrieval() {

    }

    /**
     * 获取实例的方法
     */
    public static DoubleRetrieval getInstance() {
        // 第一个 if 语句用来避免 uniqueInstance 已经被实例化之后的加锁操作
        if (instance == null) {
            // 加锁
            synchronized (DoubleRetrieval.class) {
                // 第二个 if 语句进行了加锁，所以只能有一个线程进入，就不会出现 instance == null 时两个线程同时进行实例化操作。
                if (instance == null) {
                    instance = new DoubleRetrieval();
                }
            }
        }
        return instance;
    }
}
