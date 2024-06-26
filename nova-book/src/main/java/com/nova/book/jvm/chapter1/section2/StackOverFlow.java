package com.nova.book.jvm.chapter1.section2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @description: 栈内存溢出
 * 栈帧过多、栈帧过大都会导致内存溢出，可以理解为方法 = 栈帧
 * -Xss1m
 * @author: wzh
 * @date: 2023/3/17 10:37
 */
@Slf4j(topic = "StackOverFlow")
class StackOverFlow {

    public static int count;

    /**
     * 栈帧过多
     */
    @Test
    public void demoA() {
        try {
            m1();
        } catch (Throwable e) {
            log.error("异常：", e);
            System.err.println("count = " + count);
        }
    }

    private static void m1() {
        count++;
        m1();
    }

}
