package com.nova.ebook.effectivejava.chapter9.section6;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest6 {

    /**
     * 不用延迟初始化，导致循环依赖
     */
    @Test
    public void demoA(){
        B b = new B();
    }

    @Test
    public void demoB(){
        C c = new C();
    }

}
