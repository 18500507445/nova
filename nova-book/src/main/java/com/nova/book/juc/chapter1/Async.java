package com.nova.book.juc.chapter1;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 演示同步
 * @author: wzh
 * @date: 2023/3/24 18:13
 */
@Slf4j(topic = "Async")
class Async {

    public static void main(String[] args) {
        log.debug("同步开始，time：{}", DateUtil.now());
        ThreadUtil.sleep(2000);
        log.debug("do other things...");
    }
}
