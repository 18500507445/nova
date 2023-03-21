package com.nova.book.jvm.chapter3.section4;

/**
 * @description: 类加载演示（先javac Load1.java然后javap -v Load1.class）
 * @author: wzh
 * @date: 2023/3/21 22:26
 */
class Load1 {

    static int a;

    /**
     * static块里，进行赋值，动作发生在类的构造方法中，初始化阶段
     */
    static int b = 10;

    /**
     * final：准备阶段就完成了赋值动作
     */
    static final int c = 20;
}

