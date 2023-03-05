package com.nova.book.effectivejava.chapter1.section2;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/12 21:11
 */
class Section2Test {

    @Test
    public void demoA(){
        Cat tom = Cat.builder().id(1).name("tom").build();
        System.out.println("tom = " + tom);
    }

}
