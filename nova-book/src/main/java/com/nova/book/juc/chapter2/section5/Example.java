package com.nova.book.juc.chapter2.section5;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @description: 应用案例
 * @author: wzh
 * @date: 2023/3/25 08:41
 */
@Slf4j
class Example {


    static int r = 0;

    static int r1 = 0;

    /**
     * 限制对CPU的使用
     * 在没有利用cpu来计算时，不要让while(true) 空转浪费cpu，这时可以使用yield或sleep 来让出cpu的使用权给其他程序
     *
     * @throws InterruptedException
     */
    @Test
    public void sleep() throws InterruptedException {
        while (true) {
            TimeUnit.MILLISECONDS.sleep(50);
        }
    }

    /**
     * 多个线程join
     *
     * @throws InterruptedException
     */
    @Test
    public void manyThreadJoin() throws InterruptedException {
        Thread t6 = new Thread("t6") {
            @SneakyThrows
            @Override
            public void run() {
                TimeUnit.SECONDS.sleep(1);
                r = 10;
            }
        };

        Thread t7 = new Thread("t7") {
            @SneakyThrows
            @Override
            public void run() {
                TimeUnit.SECONDS.sleep(2);
                r1 = 20;
            }
        };

        t6.start();
        t7.start();

        TimeInterval timer = DateUtil.timer();

        t7.join();
        t6.join();
        log.debug("r：{}，r1：{}，cost：{}", r, r1, timer.interval());
    }

}
