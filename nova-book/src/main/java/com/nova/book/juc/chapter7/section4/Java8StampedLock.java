package com.nova.book.juc.chapter7.section4;

import com.nova.common.utils.thread.Threads;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

/**
 * @description: 乐观读锁
 * 缺点：（1）不支持条件变量 （2）不支持可重入
 * @author: wzh
 * @date: 2023/3/30 22:01
 */
class Java8StampedLock {

    /**
     * 演示
     * 读读：没加锁
     * 读写：加锁了
     *
     * @param args
     */
    public static void main(String[] args) {
        DataContainerStamped dataContainer = new DataContainerStamped(1);
        new Thread(() -> {
            dataContainer.read(0);
        }, "t1").start();
        Threads.sleep(500);
        new Thread(() -> {
            dataContainer.read(0);
            //dataContainer.write(9999);
        }, "t2").start();
    }
}

@Slf4j(topic = "DataContainerStamped")
class DataContainerStamped {
    private int data;
    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data) {
        this.data = data;
    }

    public void read(long readTime) {
        long stamp = lock.tryOptimisticRead();
        log.debug("optimistic read locking...{}", stamp);
        Threads.sleep(readTime);
        if (lock.validate(stamp)) {
            log.debug("read finish...{}, data:{}", stamp, data);
            return;
        }
        // 锁升级 - 读锁
        log.debug("updating to read lock... {}", stamp);
        try {
            stamp = lock.readLock();
            log.debug("read lock {}", stamp);
            Threads.sleep(readTime);
            log.debug("read finish...{}, data:{}", stamp, data);
        } finally {
            log.debug("read unlock {}", stamp);
            lock.unlockRead(stamp);
        }
    }

    public void write(int newData) {
        long stamp = lock.writeLock();
        log.debug("write lock {}", stamp);
        try {
            Threads.sleep(2000);
            this.data = newData;
        } finally {
            log.debug("write unlock {}", stamp);
            lock.unlockWrite(stamp);
        }
    }
}