package com.nova.ebook.effectivejava.chapter1.section3;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/12 21:51
 */
public class Section3Test {

    @Test
    public void demoA(){
        Cat instance1 = Cat.INSTANCE;
        Cat instance2 = Cat.INSTANCE;
        System.out.println(instance2 == instance1);
    }

}
