package com.nova.spring.entity;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/12/29 14:18
 */
public class Card {

    public Card() {
        System.out.println("我是card");
    }

    public int test(String str) {
        System.out.println("我被调用了:" + str);
        return str.length();
    }
}
