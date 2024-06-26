package com.nova.book.effectivejava.chapter9.section1;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:22
 */
class StopThread {

    private static boolean stop;

    public static synchronized boolean getStop() {
        return stop;
    }

    public static synchronized void setStop() {
        stop = true;
    }

    /**
     * 主线程 stop = true
     * t线程 不知道的状态，一直循环中
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        System.err.println(DateUtil.now());
        Thread t = new Thread(() -> {
            int i = 0;
            while (!stop) {
                i++;
            }
            System.err.println(DateUtil.now());
        });
        t.start();
        ThreadUtil.sleep(1000);
        stop = true;
    }

    /**
     * 可以将private static volatile boolean stop;
     */
    @Test
    public void demoA() {

    }


    /**
     * 可以改成这样，两个同步的方法
     * 代码放到main方法中执行
     *
     * @throws InterruptedException
     */
    @Test
    public void demoB() throws InterruptedException {
        System.err.println(DateUtil.now());
        Thread t = new Thread(() -> {
            int i = 0;
            while (getStop()) {
                i++;
            }
            System.err.println(DateUtil.now());
        });
        t.start();
        ThreadUtil.sleep(1000);
        setStop();
    }


}
