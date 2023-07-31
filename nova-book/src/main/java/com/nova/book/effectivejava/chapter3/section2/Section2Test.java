package com.nova.book.effectivejava.chapter3.section2;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/15 11:11
 */
class Section2Test {

    @Test
    public void demoA(){
        Cat cat = new Cat();
        String name = cat.getName();
        System.err.println("name = " + name);
        cat.setName(null);

    }

}
