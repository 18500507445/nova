package com.nova.book.jvm.chapter3.section4;

import java.io.IOException;

/**
 * @description:
 * @author: wzh
 * @date: 2023/3/21 22:39
 */
class Load3 {

    static {
        System.err.println("main init");
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        //// 1. 静态常量不会触发初始化
        //System.err.println(B.b);
        //// 2. 类对象.class 不会触发初始化
        //System.err.println(B.class);
        //// 3. 创建该类的数组不会触发初始化
        //System.err.println(new B[0]);
        //// 4. 不会初始化类 B，但会加载 B、A
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        cl.loadClass("com.nova.book.jvm.chapter3.section4.B");
        //// 5. 不会初始化类 B，但会加载 B、A
        //ClassLoader c2 = Thread.currentThread().getContextClassLoader();
        //Class.forName("com.nova.book.jvm.chapter3.section4.B", false, c2);
        System.in.read();


        //// 1. 首次访问这个类的静态变量或静态方法时
        //System.err.println(A.a);
        //// 2. 子类初始化，如果父类还没初始化，会引发
        //System.err.println(B.c);
        //// 3. 子类访问父类静态变量，只触发父类初始化
        //System.err.println(B.a);
        //// 4. 会初始化类 B，并先初始化类 A
        //Class.forName("com.nova.book.jvm.chapter3.section4.B");


    }
}

class A {
    static int a = 0;

    static {
        System.err.println("a init");
    }
}

class B extends A {

    final static double b = 5.0;
    static boolean c = false;

    static {
        System.err.println("b init");
    }
}
