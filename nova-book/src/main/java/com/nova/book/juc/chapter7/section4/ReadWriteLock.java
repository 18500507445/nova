package com.nova.book.juc.chapter7.section4;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @description: 读写锁
 * 总结一句话：读读并发，读写、写写互斥（队列里，读的Node节点状态是Shared，写是Ex）
 * 注意事项
 * 读锁不支持条件变量
 * 重入时升级不支持：即持有读锁的情况下去获取写锁，会导致获取写锁永久等待
 * 重入时降级支持：即持有写锁的情况下去获取读锁
 * @author: wzh
 * @date: 2023/3/30 20:43
 */
class ReadWriteLock {

    public static void main(String[] args) throws InterruptedException {
        DataContainer dataContainer = new DataContainer();
        new Thread(() -> {
            final Object read = dataContainer.read();
        }, "t1").start();

        new Thread(() -> {
            dataContainer.write();
        }, "t2").start();
    }
}


@Slf4j(topic = "DataContainer")
class DataContainer {
    private Object data;
    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private final ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public Object read() {
        log.debug("获取读锁...");
        r.lock();
        try {
            log.debug("读取");
            ThreadUtil.sleep(1000);
            return data;
        } finally {
            log.debug("释放读锁...");
            r.unlock();
        }
    }

    public void write() {
        log.debug("获取写锁...");
        w.lock();
        try {
            log.debug("写入");
            ThreadUtil.sleep(1000);
        } finally {
            log.debug("释放写锁...");
            w.unlock();
        }
    }
}