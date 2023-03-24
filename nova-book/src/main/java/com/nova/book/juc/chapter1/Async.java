package com.nova.book.juc.chapter1;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 演示同步
 * @author: wzh
 * @date: 2023/3/24 18:13
 */
@Slf4j
class Async {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("同步");
        Thread.sleep(2000);
        log.debug("do other things...");
    }
}
