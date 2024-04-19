package com.nova.book.juc.chapter3.section5.lock;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 多把锁
 * @author: wzh
 * @date: 2023/3/28 09:51
 */
@Slf4j(topic = "ManyLock")
class ManyLock {

    public static void main(String[] args) {
        BigRoom bigRoom = new BigRoom();
        new Thread(() -> {
            try {
                bigRoom.study();
            } catch (InterruptedException e) {
                log.error("异常：", e);
            }
        }, "小南").start();
        new Thread(() -> {
            try {
                bigRoom.sleep();
            } catch (InterruptedException e) {
                log.error("异常：", e);
            }
        }, "小女").start();
    }
}

@Slf4j(topic = "BigRoom")
class BigRoom {
    /**
     * 一把锁
     */
    private final Object room = new Object();

    /**
     * 多把锁，synchronized锁住不同的对象
     */
    private final Object studyRoom = new Object();

    private final Object bedRoom = new Object();

    public void sleep() throws InterruptedException {
        synchronized (studyRoom) {
            log.debug("sleeping 2 小时");
            ThreadUtil.sleep(2000);
        }
    }

    public void study() throws InterruptedException {
        synchronized (bedRoom) {
            log.debug("study 1 小时");
            ThreadUtil.sleep(1);
        }
    }

}