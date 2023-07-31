package com.nova.book.effectivejava.chapter2.section2;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/14 19:43
 */
class Section2Test {

    @Test
    public void demoA() {
        Cat.BlackCat blackCat1 = new Cat.BlackCat();
        Cat.BlackCat blackCat2 = new Cat.BlackCat();
        System.err.println("hashCode1 = " + blackCat1.hashCode());
        System.err.println("hashCode2 = " + blackCat2.hashCode());
    }

    @Test
    public void demoB() {
        Cat.BlueCat blueCat = new Cat.BlueCat();
        System.err.println("hashCode = " + blueCat.hashCode());
    }
}
