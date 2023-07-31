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
class ClockExample {

    /**
     * Clock 示例
     */
    @Test
    void demoA() {
        Clock clock = Clock.systemDefaultZone();
        long millis = clock.millis();
        Instant instant = clock.instant();
        // legacy java.util.Date
        Date legacyDate = Date.from(instant);
        System.err.println(millis);
        System.err.println(legacyDate);
    }
}
