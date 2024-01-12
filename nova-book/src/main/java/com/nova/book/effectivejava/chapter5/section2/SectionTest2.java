package com.nova.book.effectivejava.chapter5.section2;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest2 {

    @Test
    public void demoA() {
        System.err.println(WeekEnum.ONE.ordinal());
    }

    @Test
    public void demoB() {
        System.err.println(WeekEnum.valuesOf(1));
    }


}
