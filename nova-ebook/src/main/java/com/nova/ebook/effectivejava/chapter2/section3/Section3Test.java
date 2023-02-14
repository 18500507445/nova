package com.nova.ebook.effectivejava.chapter2.section3;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/14 19:23
 */
public class Section3Test {

    @Test
    public void demoA() {
        Cat.BlackCat blackCat = new Cat.BlackCat();
        System.out.println("blackCat = " + blackCat);
    }

    @Test
    public void demoB() {
        Cat.BlueCat blueCat = new Cat.BlueCat();
        System.out.println("blueCat = " + blueCat);
    }


}
