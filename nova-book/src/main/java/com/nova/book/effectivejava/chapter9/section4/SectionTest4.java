package com.nova.book.effectivejava.chapter9.section4;

import cn.hutool.json.JSONUtil;
import com.nova.common.utils.thread.Threads;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest4 {

    @Test
    public void demoA() {
        Map<Integer, Integer> hashMap = new HashMap<>(16);
        hashMap.put(1, 10);
        hashMap.put(2, 20);

        hashMap.computeIfAbsent(1, k -> k * 2);
        hashMap.computeIfAbsent(2, k -> k * 2);
        hashMap.computeIfAbsent(3, k -> k * 2);

        System.out.println("hashMap = " + JSONUtil.toJsonStr(hashMap));
    }

    /**
     * c是new在堆当中，而a和b中的1是在字符串池中
     */
    @Test
    public void demoB() {
        String a = "1";
        String b = "1";
        String c = new String("1");

        System.out.println(a == b);
        System.out.println(a == c);
        System.out.println(a.intern() == c.intern());
    }

    /**
     * 使用同步器：CountDownLatch
     * 模拟了8个运动员跑步，随机成绩需要等待8个运动员都结束之后，才能统计成绩
     */
    @Test
    public void demoC() throws InterruptedException {
        int s = 8;
        //计数器 每跑完一个人就countDown(-1)，到0就可以await
        CountDownLatch cd = new CountDownLatch(s);
        Random r = new Random();
        int baseMs = 5000;
        AtomicInteger total = new AtomicInteger();
        ExecutorService executor = Executors.newFixedThreadPool(s);
        for (int i = 0; i < s; i++) {
            executor.submit(() -> {
                int f = baseMs + r.nextInt(2000);
                Threads.sleep(f);
                total.addAndGet(f);

                System.out.println("运动员：" + Thread.currentThread().getName() + "，成绩为：" + f);
                cd.countDown();
            });
        }
        cd.await();
        System.out.println("全部结束，统计成绩，平均成绩为：" + total.get() / s);
        System.exit(0);
    }

    /**
     * Semaphore信号量
     * 模拟开启8个线程，但是一次使用2个。
     *
     * @throws InterruptedException
     */
    @Test
    public void demoD() throws InterruptedException {
        int num = 8;
        CountDownLatch cd = new CountDownLatch(num);
        Semaphore sp = new Semaphore(2);
        Random r = new Random();
        ExecutorService executor = Executors.newFixedThreadPool(num);
        for (int i = 0; i < num; i++) {
            executor.submit(() -> {
                try {
                    sp.acquire();
                    System.out.println(Thread.currentThread().getName() + "，开始执行");
                    Thread.sleep(r.nextInt(2000));
                    System.out.println(Thread.currentThread().getName() + "，结束执行");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    cd.countDown();
                    sp.release();
                }
            });
        }
        cd.await();
        System.out.println("全部结束，退出程序");
        System.exit(0);
    }
}
