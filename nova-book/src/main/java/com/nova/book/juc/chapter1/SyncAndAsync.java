package com.nova.book.juc.chapter1;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author: wzh
 * @description: 同步和异步
 * @date: 2024/04/02 14:36
 */
@Slf4j(topic = "SyncAndAsync")
public class SyncAndAsync {

    @Test
    public void sync() {
        new Thread(() -> {
            log.info("异步开始，time：{}", DateUtil.now());
            ThreadUtil.sleep(2000);
        }).start();
        log.info("do other things...");
    }

    @Test
    public void async() {
        log.info("同步开始，time：{}", DateUtil.now());
        ThreadUtil.sleep(2000);
        log.info("do other things...");
    }


}
