package com.nova.book.effectivejava.chapter3.section10;

import lombok.Data;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 13:40
 */
public class Cat {

    /**
     * 静态成员类
     */
    static class BlueCat {

    }

    /**
     * 非静态成员类-内部类
     */
    class BlackCat {

    }

    /**
     * 方法内部的类
     * (1)同局部变量一样，无法使用访问修饰符
     * (2)作用域在方法内，方法外无法使用
     * (3)无法声明static属性和方法
     */
    public void method() {
        @Data
        class BlackCat {
            private String name;
        }
    }

}

