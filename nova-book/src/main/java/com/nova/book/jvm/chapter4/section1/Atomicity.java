package com.nova.book.jvm.chapter4.section1;

/**
 * @description: 原子性，synchronized（也能保证可见性，但是它属于重量级锁）
 * @author: wzh
 * @date: 2023/3/22 21:50
 */
class Atomicity {

    static int i = 0;

    /**
     * obj相当于一个房间、synchronized就是一把锁，t1线程进入房间执行完synchronized代码块后，t1出房间，t2进入后反锁大门
     */
    static final Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            //锁的粒度粗化，减少了线程加锁、解锁
            synchronized (obj) {
                for (int j = 0; j < 50000; j++) {
                    i++;
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (obj) {
                for (int j = 0; j < 50000; j++) {
                    i--;
                }
            }
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.err.println(i);
    }
}
