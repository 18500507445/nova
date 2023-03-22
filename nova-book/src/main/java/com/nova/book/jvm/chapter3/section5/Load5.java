package com.nova.book.jvm.chapter3.section5;

/**
 * @description: 双亲委派原则
 * @author: wzh
 * @date: 2023/3/22 21:31
 */
public class Load5 {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(Load5.class.getClassLoader());
        Class<?> aClass = Load5.class.getClassLoader().loadClass("com.nova.book.jvm.chapter3.section5.G");
        System.out.println(aClass.getClassLoader());
    }
}

class G {

    static {
        System.out.println("Classpath G init");
    }
}

