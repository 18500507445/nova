package com.nova.book.effectivejava.chapter1.section7;


import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/13 16:52
 */
class Section7Test {

    @Test
    public void demoA() throws Throwable {
        Cat cat = new Cat();
        cat.finalize();
    }

}
