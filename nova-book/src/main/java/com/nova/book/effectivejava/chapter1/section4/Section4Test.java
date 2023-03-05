package com.nova.book.effectivejava.chapter1.section4;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/13 16:49
 */
class Section4Test {

    @Test
    public void demoA() {
        boolean isCat = CatUtil.isCat("tom");
        System.out.println("isCat = " + isCat);
    }

}
