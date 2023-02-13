package com.nova.ebook.effectivejava.chapter2.section1;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/13 19:44
 */
public class Section1Test {

    /**
     * 模拟两次数据是从数据库查出来的，需要做比较
     * 没重写equals的时候，返回false
     * 重写后返回true
     */
    @Test
    public void demoA() {
        Cat a = new Cat(1, "wzh", "red");
        Cat b = new Cat(1, "wzh", "red");
        System.out.println(a.equals(b));
    }

    @Test
    public void demoB(){
        Cat a = new Cat(1, "wzh", "black");
        BlackCat b = new BlackCat();
        b.setId(1);
        b.setName("wzh");

        System.out.println(a.equals(b));
    }


}
