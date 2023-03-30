package com.nova.tools.java8.datetime;

import org.junit.jupiter.api.Test;

import java.time.Instant;

/**
 * Instant 示例
 *
 * @author wzh
 * @date 2018/3/2
 */
class InstantExample {

    @Test
    void testA() {
        // 创建一个Instant实例
        Instant now = Instant.now();

        // 访问Instant的时间
        long seconds = now.getEpochSecond();
        int nanos = now.getNano();
        System.out.println("seconds : " + seconds);
        System.out.println("nanos   : " + nanos);

        // 3秒后
        Instant later = now.plusSeconds(3);
        // 3秒前
        Instant earlier = now.minusSeconds(3);

        System.out.println("current : " + now.toString());
        System.out.println("later   : " + later);
        System.out.println("earlier : " + earlier);
    }

}
