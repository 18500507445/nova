package com.nova.ebook.effectivejava.chapter1.section1;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/12 15:06
 */
class Section1Test {

    public static final int NUM = 3;

    @Test
    public void demoA() {
        for (int i = 1; i <= NUM; i++) {
            Cat singleton = Cat.getSingleton();
            System.out.println("singleton = " + singleton);
            System.out.println("hashCode:" + singleton.hashCode());
        }
    }

    @Test
    public void demoB() {
        for (int i = 1; i <= NUM; i++) {
            Cat singleton = Cat.getSingleton();
            singleton.setId(i);
            singleton.setName(i + "");
            System.out.println("singleton = " + singleton);
            System.out.println("hashCode:" + singleton.hashCode());
        }
    }

    @Test
    public void demoC() {
        for (int i = 1; i <= NUM; i++) {
            Cat singleton = Cat.getNotRepeat(i + "");
            System.out.println("singleton = " + singleton);
            System.out.println("hashCode:" + singleton.hashCode());
        }

        Cat singleton = Cat.getNotRepeat("1");
        System.out.println(singleton);
        System.out.println("hashCode:" + singleton.hashCode());
    }
}
