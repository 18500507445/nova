package com.nova.book.juc.chapter6.section1;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * @description: 日期安全
 * @author: wzh
 * @date: 2023/3/29 15:23
 */
@Slf4j(topic = "DateSafe")
public class DateSafe {

    public static void main(String[] args) {
        safe();

        unSafe();
    }

    private static void safe() {
        DateTimeFormatter stf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                TemporalAccessor parse = stf.parse("1951-04-21");
                log.debug("{}", parse);
            }).start();
        }
    }

    /**
     * 加锁-synchronized
     */
    private static void unSafe() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronized (sdf) {
                    try {
                        log.debug("{}", sdf.parse("1951-04-21"));
                    } catch (Exception e) {
                        log.error("{}", e);
                    }
                }
            }).start();
        }
    }
}
