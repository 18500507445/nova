package com.nova.ebook.effectivejava.chapter8.section1;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest1 {

    @Test
    public void demoA() {
        Integer[] arr = new Integer[5];
        int i = 0;
        try {
            while (true) {
                arr[i++] = i++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

}
