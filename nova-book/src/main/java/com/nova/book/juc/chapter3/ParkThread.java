package com.nova.book.juc.chapter3;

import com.nova.common.utils.thread.Threads;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @description: LockSupport.park对比
 * 与Object的wait&notify相比
 * (1)wait，notify 和 notifyAll 必须配合 Object Monitor一起使用，而park/unPark不必
 * (2)park&unPark是以线程为单位来【阻塞】和【唤醒】线程，而notify只能随机唤醒一个等待线程，notifyAll是唤醒所有等待线程，就不那么【精确】
 * (3)park&unPark可以先unPark，而wait&notify不能先notify
 * @author: wzh
 * @date: 2023/3/27 10:26
 */
@Slf4j(topic = "park")
class ParkThread {

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            log.debug("start...");
            Threads.sleep(1000);
            log.debug("park...");
            LockSupport.park();
            log.debug("resume...");
        }, "t1");
        t1.start();

        Threads.sleep(2000);
        log.debug("unPark...");
        LockSupport.unpark(t1);
    }
}
