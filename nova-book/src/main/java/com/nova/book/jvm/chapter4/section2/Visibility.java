package com.nova.book.jvm.chapter4.section2;

import com.nova.common.utils.thread.Threads;

/**
 * @description: volatile只能保证可见性，不能保证原子性
 * 场景：仅用在一个写线程，多个读线程的情况，性能比synchronized强
 * @author: wzh
 * @date: 2023/3/22 22:29
 */
class Visibility {

    volatile static boolean run = true;

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            while (run) {
                // ....
            }
        });
        t.start();

        Threads.sleep(1000);
        // 如果不加volatile关键字，线程t不会如预想的停下来
        run = false;
        System.err.println("线程停止");
    }
}
