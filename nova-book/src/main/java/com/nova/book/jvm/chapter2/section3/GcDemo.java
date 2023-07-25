package com.nova.book.jvm.chapter2.section3;

import cn.hutool.core.thread.ThreadUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @description: 演示gc
 * @author: wzh
 * @date: 2023/3/19 21:40
 */
public class GcDemo {

    private static final int _512KB = 512 * 1024;
    private static final int _1MB = 1024 * 1024;
    private static final int _6MB = 6 * 1024 * 1024;
    private static final int _7MB = 7 * 1024 * 1024;
    private static final int _8MB = 8 * 1024 * 1024;

    /**
     * idea运行找edit Configuration 找到modify options 点开 vm options 添加参数如下
     * -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -verbose:gc -XX:-ScavengeBeforeFullGC
     * 伊甸园：幸存form：幸存to比例为，8：1：1，所以执行程序，total空间为9m，幸存to区需要空出来
     * <p>
     * 可以不调用方法直接执行，先观察下参数，新生代和老年代内存分配
     * <p>
     * 新生代总容量9m，伊甸园区8m，已使用91%
     * <p>
     * 如果内存溢出发生在一个线程里面呢，会不会影响其它线程运行
     * 答：当一个线程抛出oom异常后，它所占据的内存资源会全部被释放掉，从而不会影响其它的线程运行
     */
    public static void main(String[] args) {
        gc();
//        tenured();
//        direct();
//        outOfMemory();
    }


    /**
     * 添加数据超过伊甸园区的空间，观看发生minor gc情况
     */
    public static void gc() {
        ArrayList<byte[]> list = new ArrayList<>();
        list.add(new byte[_7MB]);
    }

    /**
     * 继续添加，gc后，7m对象，进入老年代
     */
    public static void tenured() {
        ArrayList<byte[]> list = new ArrayList<>();
        list.add(new byte[_7MB]);
        list.add(new byte[_1MB]);
    }

    /**
     * 超过伊甸园区内存空间的大对象，不会发生gc，直接进入老年代
     */
    public static void direct() {
        ArrayList<byte[]> list = new ArrayList<>();
        list.add(new byte[_8MB]);
    }

    /**
     * 放入的数据>（伊甸园区剩余空间+老年代剩余空间）
     * 那么就会发生内存溢出，当然内存溢出之前，做了两次努力，minor gc和full gc之后发现还不够
     */
    public static void outOfMemory() {
        ArrayList<byte[]> list = new ArrayList<>();
        list.add(new byte[_8MB]);
        list.add(new byte[_8MB]);
    }


}
