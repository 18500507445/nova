package com.nova.tools.demo.designpatterns.single;

/**
 * @Description: 单例模式实现方式有好多种，但大部分都会有多线程环境下的问题；
 * 使用内部类可以避免这个问题，因为在多线程环境下，jvm对一个类的初始化会做限制，
 * 同一时间只会允许一个线程去初始化一个类，这样就从虚拟机层面避免了大部分单例实现的问题，可以尝试下
 * @Author: wangzehui
 * @Date: 2022/8/6 13:36
 */
public class Singleton {

    private static class SingletonHolder {
        public static final Singleton INSTANCE = new Singleton();
    }

    private Singleton() {
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void showMessage() {
        System.out.println("Hello world");
    }
}
