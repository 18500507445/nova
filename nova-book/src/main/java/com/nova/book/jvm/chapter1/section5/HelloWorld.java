package com.nova.book.jvm.chapter1.section5;

/**
 * @description: 方法区 常量池演示
 * 二进制字节码（类基本信息，常量池，类方法定义，包含了虚拟机指令）
 * 进入到当前目录../nova-book/src/main/java/com/nova/book/jvm
 * 第一步:javac HelloWorld.java
 * 第二步:javap -c HelloWorld.class
 * @author: wzh
 * @date: 2023/3/17 16:58
 */
class HelloWorld {

    public static void main(String[] args) {
        System.out.println("hello world");
    }

}
