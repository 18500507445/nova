package com.nova.book.effectivejava.chapter2.section3;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/14 19:23
 */
class Section3Test {

    @Test
    public void demoA() {
        Cat.BlackCat blackCat = new Cat.BlackCat();
        System.err.println("blackCat = " + blackCat);
    }

    @Test
    public void demoB() {
        Cat.BlueCat blueCat = new Cat.BlueCat();
        System.err.println("blueCat = " + blueCat);
    }


}
