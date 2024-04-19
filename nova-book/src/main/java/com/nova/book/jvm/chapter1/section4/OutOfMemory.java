package com.nova.book.jvm.chapter1.section4;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 堆内存溢出演示
 * -Xmx8m
 * @author: wzh
 * @date: 2023/3/17 15:30
 */
@Slf4j(topic = "OutOfMemory")
class OutOfMemory {

    /**
     * Java heap space
     *
     * @param args
     */
    public static void main(String[] args) {
        int i = 0;
        try {
            List<String> list = new ArrayList<>();
            String a = "hello";
            while (true) {
                list.add(a);
                a = a + a;
                i++;
            }
        } catch (Throwable e) {
            log.error("异常：", e);
            System.err.println("i = " + i);
        }
    }
}
