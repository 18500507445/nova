package com.nova.book.jvm.chapter3.section5;

/**
 * @description: 扩展类加载器
 * @author: wzh
 * @date: 2023/3/22 21:23
 */
class Load4 {

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.nova.book.jvm.chapter3.section5.F");
        System.out.println(aClass.getClassLoader());
    }
}

class F {

    static {
        System.out.println("Classpath F init");
    }
}
