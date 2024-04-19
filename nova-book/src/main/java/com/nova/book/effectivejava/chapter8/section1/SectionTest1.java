package com.nova.book.effectivejava.chapter8.section1;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
@Slf4j(topic = "SectionTest1")
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
            log.error("异常：", e);
        }
    }

}
