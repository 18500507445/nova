package com.nova.ebook.effectivejava.chapter7.section1;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:22
 */
class Cat {

    public static int NUM = 1;

    @Test
    public void demoA() {
        for (int i = 0; i < 100; i++) {
            NUM++;
        }
        System.out.println(NUM);
    }


}
