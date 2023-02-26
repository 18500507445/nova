package com.nova.ebook.effectivejava.chapter6.section2;

import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest2 {

    /**
     * 仍然能获取finalClass对象Date进行修改属性
     */
    @Test
    public void demoA() {
        Cat cat1 = new Cat(new Date());
        Date date = cat1.getDate();
        date.setTime(System.currentTimeMillis());
        System.out.println("cat1 = " + cat1);
    }

}
