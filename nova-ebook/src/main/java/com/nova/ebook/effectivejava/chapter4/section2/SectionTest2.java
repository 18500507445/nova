package com.nova.ebook.effectivejava.chapter4.section2;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest2 {

    @Test
    public void demoA(){
        Cache.add("1",new Object());

        Object o = Cache.get("1");
        System.out.println(o);
    }

}
