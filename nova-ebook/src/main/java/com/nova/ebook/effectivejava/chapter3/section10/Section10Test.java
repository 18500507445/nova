package com.nova.ebook.effectivejava.chapter3.section10;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/15 20:58
 */
class Section10Test {

    public static final String str1 = "str1";

    public String str2 = "str2";


    /**
     * blueCat是静态成员类，可以直接new
     * blackCat非静态，需要先new外部类Cat再new内部类BlackCat
     */
    @Test
    public void demoA() {
        Cat.BlueCat blueCat = new Cat.BlueCat();

        Cat.BlackCat blackCat = new Cat().new BlackCat();

    }

    @Test
    public void demoB() {
        System.out.println(str1);
    }

    /**
     * 匿名内部类
     */
    @Test
    public void demoC() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("running");
            }
        };
        new Thread(runnable).start();
    }

}
