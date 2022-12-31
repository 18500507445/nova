package com.nova.design.create.singleton.lazy;

/**
 * @description: 懒汉模式-线程安全
 * @author: wzh
 * @date: 2022/12/31 14:07
 */
public class LazySafe {

    /**
     * 私有实例
     */
    private static LazySafe instance;

    /**
     * 私有构造方法
     */
    private LazySafe() {
    }

    /**
     * 获取实例的方法，该方法使用synchronized加锁，来保证线程安全性
     */
    public static synchronized LazySafe getInstance() {
        if (instance == null) {
            instance = new LazySafe();
        }
        return instance;
    }
}
