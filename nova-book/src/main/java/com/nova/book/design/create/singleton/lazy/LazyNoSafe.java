package com.nova.book.design.create.singleton.lazy;

/**
 * @description: 懒汉模式-线程不安全
 * 用到的时候才去检查有没有实例，如果有则直接返回，没有则新建。
 * 在多线程下，两个线程同时进入了if (instance == null) 判断语句块，这时便会产生多个实例。
 * @author: wzh
 * @date: 2022/12/31 14:07
 */
public class LazyNoSafe {

    /**
     * 私有实例
     */
    private static LazyNoSafe instance;

    /**
     * 私有构造方法
     */
    private LazyNoSafe() {
    }

    /**
     * 获取实例的方法
     */
    public static LazyNoSafe getInstance() {
        if (instance == null) {
            instance = new LazyNoSafe();
        }
        return instance;
    }
}
