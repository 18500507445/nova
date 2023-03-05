package com.nova.book.effectivejava.chapter3.section1;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/15 11:11
 */
class Section1Test {

    @Test
    public void demoA() {
        Cat cat = new Cat();
        Integer id = cat.id;

        Cat.BlackCat blackCat = new Cat.BlackCat();
    }

}
