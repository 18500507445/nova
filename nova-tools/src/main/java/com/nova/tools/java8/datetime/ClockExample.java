package com.nova.tools.java8.datetime;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

/**
 * @description: dateTimeDemo类
 * @author: wzh
 * @date: 2022/11/18 13:53
 */
public class ClockExample {

    /**
     * Clock 示例
     */
    @Test
    public void demoA() {
        Clock clock = Clock.systemDefaultZone();
        long millis = clock.millis();
        Instant instant = clock.instant();
        // legacy java.util.Date
        Date legacyDate = Date.from(instant);
        System.out.println(millis);
        System.out.println(legacyDate);
    }
}
