package com.nova.book.juc.chapter1;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.nova.common.utils.thread.Threads;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 演示异步
 * @author: wzh
 * @date: 2023/3/24 18:18
 */
@Slf4j(topic = "Sync")
class Sync {

    public static void main(String[] args) {
        new Thread(() -> {
            log.debug("异步开始，time：{}", DateUtil.now());
            Threads.sleep(2000);
        }).start();
        log.debug("do other things...");
    }
}
