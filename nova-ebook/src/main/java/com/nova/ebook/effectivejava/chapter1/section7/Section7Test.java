package com.nova.ebook.effectivejava.chapter1.section7;


import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/13 16:52
 */
public class Section7Test {

    @Test
    public void demoA() throws Throwable {
        Cat cat = new Cat();
        cat.finalize();
    }

}
