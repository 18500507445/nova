package com.nova.tools.java8.datetime;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Duration 示例
 *
 * @author: wzh
 * @date: 2018/3/3
 */
class DurationExample {

    @Test
    void demoA() {
        // 创建Duration实例
        Instant first = Instant.now();
        ThreadUtil.sleep(3000);
        Instant second = Instant.now();
        Duration duration = Duration.between(first, second);

        // 访问Duration的时间
        long seconds = duration.getSeconds();

        System.err.println("相差 : " + seconds + " 秒");

        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusDays(5);
        Duration duration2 = Duration.between(from, to);

        System.err.println("从 from 到 to 相差 : " + duration2.toDays() + " 天");
    }

}
