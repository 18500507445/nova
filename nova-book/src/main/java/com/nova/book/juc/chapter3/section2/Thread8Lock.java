package com.nova.book.juc.chapter3.section2;

import com.nova.common.utils.thread.Threads;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 线程8锁
 * @author: wzh
 * @date: 2023/3/25 16:15
 */
class Thread8Lock {

    /**
     * 注意，synchronized作用方法上是锁住当前对象this，static synchronized相当于锁住的Class类对象，二者区分开
     *
     * @param args
     */
    public static void main(String[] args) {
        //oneLock();
        //twoLock();
        //threeLock();
        //fourLock();
        //fiveLock();
        //sixLock();
        //sevenLock();
        //eightLock();
    }

    /**
     * 12或者21
     */
    public static void oneLock() {
        NumberOne n1 = new NumberOne();
        new Thread(() -> {
            n1.a();
        }).start();
        new Thread(() -> {
            n1.b();
        }).start();
    }


    /**
     * 1s后12，或2 1s后1
     */
    public static void twoLock() {
        NumberTwo n2 = new NumberTwo();
        new Thread(() -> {
            n2.a();
        }).start();
        new Thread(() -> {
            n2.b();
        }).start();
    }

    /**
     * 3 1s 12
     * 23 1s 1
     * 32 1s 1
     */
    public static void threeLock() {
        NumberThree n3 = new NumberThree();
        new Thread(() -> {
            n3.a();
        }).start();
        new Thread(() -> {
            n3.b();
        }).start();
        new Thread(() -> {
            n3.c();
        }).start();
    }

    /**
     * 2 1s 后 1
     */
    public static void fourLock() {
        NumberTwo n1 = new NumberTwo();
        NumberTwo n2 = new NumberTwo();
        new Thread(() -> {
            n1.a();
        }).start();
        new Thread(() -> {
            n2.b();
        }).start();
    }

    /**
     * 2 1s 后 1
     */
    public static void fiveLock() {
        NumberFive n1 = new NumberFive();
        new Thread(() -> {
            NumberFive.a();
        }).start();
        new Thread(() -> {
            n1.b();
        }).start();
    }

    /**
     * 1s后12，或2 1s后 1
     */
    public static void sixLock() {
        new Thread(() -> {
            NumberSix.a();
        }).start();
        new Thread(() -> {
            NumberSix.b();
        }).start();
    }

    /**
     * 2 1s后1
     */
    public static void sevenLock() {
        NumberFive n1 = new NumberFive();
        new Thread(() -> {
            NumberFive.a();
        }).start();
        new Thread(() -> {
            n1.b();
        }).start();
    }

    /**
     * 1s后12，或2 1s后1
     */
    public static void eightLock() {
        new Thread(() -> {
            NumberSix.a();
        }).start();
        new Thread(() -> {
            NumberSix.b();
        }).start();
    }
}

@Slf4j
class NumberOne {
    public synchronized void a() {
        log.debug("1");
    }

    public synchronized void b() {
        log.debug("2");
    }
}

@Slf4j
class NumberTwo {
    @SneakyThrows
    public synchronized void a() {
        Threads.sleep(1000);
        log.debug("1");
    }

    public synchronized void b() {
        log.debug("2");
    }
}

@Slf4j
class NumberThree {

    @SneakyThrows
    public synchronized void a() {
        Threads.sleep(1000);
        log.debug("1");
    }

    public synchronized void b() {
        log.debug("2");
    }

    public void c() {
        log.debug("c");
    }
}

@Slf4j
class NumberFive {

    @SneakyThrows
    public static synchronized void a() {
        Threads.sleep(1000);
        log.debug("1");
    }

    public synchronized void b() {
        log.debug("2");
    }

}

@Slf4j
class NumberSix {

    @SneakyThrows
    public static synchronized void a() {
        Threads.sleep(1000);
        log.debug("1");
    }

    public static synchronized void b() {
        log.debug("2");
    }

}
