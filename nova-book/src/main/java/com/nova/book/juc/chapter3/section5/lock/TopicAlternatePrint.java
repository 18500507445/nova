package com.nova.book.juc.chapter3.section5.lock;

import com.nova.common.utils.thread.Threads;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 交替输出
 * @author: wzh
 * @date: 2023/3/29 12:34
 */
@Slf4j(topic = "TopicAlternatePrint")
class TopicAlternatePrint {

    static Thread t1;
    static Thread t2;
    static Thread t3;

    public static void main(String[] args) {

        //condition();

        park();
    }

    public static void condition() {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();
        new Thread(() -> {
            awaitSignal.print("a", a, b);
        }).start();
        new Thread(() -> {
            awaitSignal.print("b", b, c);
        }).start();
        new Thread(() -> {
            awaitSignal.print("c", c, a);
        }).start();

        Threads.sleep(1000);
        awaitSignal.lock();
        try {
            System.err.println("开始...");
            a.signal();
        } finally {
            awaitSignal.unlock();
        }
    }

    public static void park() {
        ParkUnPark pu = new ParkUnPark(5);
        t1 = new Thread(() -> {
            pu.print("a", t2);
        });
        t2 = new Thread(() -> {
            pu.print("b", t3);
        });
        t3 = new Thread(() -> {
            pu.print("c", t1);
        });
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);
    }
}

class AwaitSignal extends ReentrantLock {

    private final int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    /**
     * 参数1 打印内容， 参数2 进入哪一间休息室, 参数3 下一间休息室
     *
     * @param str
     * @param current
     * @param next
     */
    public void print(String str, Condition current, Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                current.await();
                System.err.print(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }
}

class ParkUnPark {

    private final int loopNumber;

    public ParkUnPark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Thread next) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.err.print(str);
            LockSupport.unpark(next);
        }
    }
}