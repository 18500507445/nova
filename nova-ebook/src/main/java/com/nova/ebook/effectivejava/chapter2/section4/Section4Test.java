package com.nova.ebook.effectivejava.chapter2.section4;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/14 19:49
 */
class Section4Test {

    @Test
    public void demoA() throws Exception {
        Cat cat1 = new Cat();
        Cat cat2 = new Cat(1, "tom", "blue");
        Cat.class.getConstructor().newInstance();
    }

    /**
     * 绕过了构造器，直接创建了cat2对象
     * @throws CloneNotSupportedException
     */
    @Test
    public void demoB() throws CloneNotSupportedException {
        Cat cat1 = new Cat();
        cat1.setName("one");
        System.out.println(cat1);

        Cat cat2 = cat1.clone();
        cat2.setName("two");
        System.out.println(cat2);
    }


}
