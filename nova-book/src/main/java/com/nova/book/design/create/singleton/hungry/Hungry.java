package com.nova.book.design.create.singleton.hungry;


/**
 * @description: 饿汉模式(浪费性能)
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class Hungry {

    /**
     * 私有实例，静态变量会在类加载的时候初始化，是线程安全的
     */
    private static final Hungry instance = new Hungry();

    /**
     * 私有构造方法
     */
    private Hungry() {
    }

    /**
     * 获取实例的方法
     */
    public static Hungry getInstance() {
        return instance;
    }

    /**
     * 类方法
     */
    public void process() {

    }

}
