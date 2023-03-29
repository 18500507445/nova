package com.nova.book.effectivejava.chapter9.section1;

import com.nova.common.utils.thread.Threads;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/28 18:18
 */
public class VolatileThread {

    private static volatile int count = 0;

    private static int sum = 0;

    /**
     * 多次执行count值小于预期值
     * 可见volatile关键字只可见、不提供锁和互斥操作
     * <p>
     * 可见：内存可见性，任何线程修改，立刻被其它线程看到
     *
     * @throws InterruptedException
     */
    @Test
    public void demoA() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int i1 = 0; i1 < 1000; i1++) {
                    count++;
                }
            }).start();
        }
        Threads.sleep(1000);
        System.out.println("count = " + count);
    }

    /**
     * 可以不用volatile
     * 方法里加synchronized
     *
     * @throws InterruptedException
     */
    @Test
    public void demoB() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int i1 = 0; i1 < 1000; i1++) {
                    synchronized (VolatileThread.class) {
                        sum++;
                    }
                }
            }).start();
        }
        Threads.sleep(1000);
        System.out.println("sum = " + sum);
    }


}
