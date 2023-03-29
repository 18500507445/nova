package com.nova.book.design.create.singleton.inner;

/**
 * @description: 静态内部类
 * @author: wangzehui
 * @date: 2022/12/31 14:32
 */
public class InnerClass {

    /**
     * 私有构造方法
     */
    private InnerClass() {
    }

    /**
     * 取实例的方法
     */
    public static InnerClass getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 类方法
     */
    public void process() {

    }

    /**
     * 私有静态内部类
     */
    private static class SingletonHolder {
        private static final InnerClass INSTANCE = new InnerClass();
    }
}
