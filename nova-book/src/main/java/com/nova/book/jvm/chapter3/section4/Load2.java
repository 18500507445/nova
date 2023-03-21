package com.nova.book.jvm.chapter3.section4;

import java.io.IOException;

/**
 * @description: loadClass 方法不会导致类的解析和初始化
 * @author: wzh
 * @date: 2023/3/21 22:35
 */
class Load2 {

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        ClassLoader classloader = Load2.class.getClassLoader();
        Class<?> c = classloader.loadClass("com.nova.book.jvm.chapter3.section4.C");
        System.in.read();
    }
}

class C {
    D d = new D();
}

class D {

}