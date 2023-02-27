package com.nova.ebook.effectivejava.chapter8.section6;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest6 {

    @Test
    public void demoA() {
        //空指针异常打印
        get(null, 1);
    }

    @Test
    public void demoB() {
        int[] iss = new int[1];
        //数组下标砰界异常
        get(iss, 3);
    }

    public static int get(int[] intArray, int index) {
        return intArray[index];

    }
}
