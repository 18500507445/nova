package com.nova.book.effectivejava.chapter3.section4;

import lombok.Data;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/15 19:01
 */
@Data
class Cat {

    /**
     * 方法1
     */
    public void m1() {

    }

    public void m2() {

    }

}

/**
 * 继承
 */
class BlueCat extends Cat {

}

/**
 * 复合
 */
class BlackCat {

    private Cat cat;

    public void m1() {
        cat.m1();
    }

    public void m2() {
        cat.m2();
    }

}
