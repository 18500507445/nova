package com.nova.book.juc.chapter3.section2;

import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: synchronized线程6锁
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
        oneLock();
//        twoLock();
//        threeLock();
        //fourLock();
        //fiveLock();
        //sixLock();
    }

    /**
     * new出对象调用方法，方法是普通方法，所以锁的是当前new出的对象，ab方法共享一把锁
     * 两种情况（1）先a后b（2）先b后a
     * 取决于哪个线程先占锁
     */
    public static void oneLock() {
        NumberOne n1 = new NumberOne();
        new Thread(n1::a).start();
        new Thread(n1::b).start();
    }


    /**
     * 共享n2对象这一把锁，两种情况，（1）1秒后a、b，（2）先b、1秒后a
     * 取决于哪个线程先占锁
     */
    public static void twoLock() {
        NumberTwo n2 = new NumberTwo();
        new Thread(n2::a).start();
        new Thread(n2::b).start();
    }

    /**
     * c方法同步，肯定优先执行（情况3），如果b方法先占锁就是（情况2）
     * （1）c 1s ab
     * （2）bc 1s a
     * （3）cb 1s a
     */
    public static void threeLock() {
        NumberThree n3 = new NumberThree();
        new Thread(n3::a).start();
        new Thread(n3::b).start();
        new Thread(n3::c).start();
    }

    /**
     * n1，n2两个对象锁，a有sleep所以只有一种情况：a 1s后 b
     */
    public static void fourLock() {
        NumberTwo n1 = new NumberTwo();
        NumberTwo n2 = new NumberTwo();
        new Thread(n1::a).start();
        new Thread(n2::b).start();
    }

    /**
     * 一个全局class锁，一个对象n1锁
     * b 1s后 a
     */
    public static void fiveLock() {
        NumberFive n1 = new NumberFive();
        new Thread(NumberFive::a).start();
        new Thread(n1::b).start();
    }

    /**
     * ab方法使用同一个全局class锁
     * 两种情况：（1）1s后ab，（2）b，1s后a
     */
    public static void sixLock() {
        new Thread(NumberSix::a).start();
        new Thread(NumberSix::b).start();
    }

}

@Slf4j
class NumberOne {
    public synchronized void a() {
        System.err.println("a");
    }

    public synchronized void b() {
        System.err.println("b");
    }
}

@Slf4j
class NumberTwo {
    @SneakyThrows
    public synchronized void a() {
        ThreadUtil.sleep(1000);
        System.err.println("a");
    }

    public synchronized void b() {
        System.err.println("b");
    }
}

@Slf4j
class NumberThree {

    @SneakyThrows
    public synchronized void a() {
        ThreadUtil.sleep(1000);
        System.err.println("a");
    }

    public synchronized void b() {
        System.err.println("b");
    }

    public void c() {
        System.err.println("c");
    }
}

@Slf4j
class NumberFive {

    @SneakyThrows
    public static synchronized void a() {
        ThreadUtil.sleep(1000);
        System.err.println("a");
    }

    public synchronized void b() {
        System.err.println("b");
    }

}

@Slf4j
class NumberSix {

    @SneakyThrows
    public static synchronized void a() {
        ThreadUtil.sleep(1000);
        System.err.println("a");
    }

    public static synchronized void b() {
        System.err.println("b");
    }

}
