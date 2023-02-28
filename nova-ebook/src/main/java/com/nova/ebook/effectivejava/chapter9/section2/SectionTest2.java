package com.nova.ebook.effectivejava.chapter9.section2;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest2 {


    @Test
    public void demoA() throws Exception {
        Pool pool = new Pool(new LinkedList<Integer>() {
            @Override
            public boolean add(Integer s) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return super.add(s);
            }
        });

        for (int i = 0; i < 16; i++) {
            final int x = i;
            new Thread(() -> pool.add(x)).start();
        }
        Thread.sleep(4000);
        System.out.println("pool.size() = " + pool.size());
    }

    @Test
    public void demoB() throws Exception {
        TimeInterval timer = DateUtil.timer();
        Pool pool = new Pool(new LinkedList<>());
        for (int i = 0; i < 16; i++) {
            final int x = i;
            new Thread(() -> {
                try {
                    pool.add(x);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Thread.sleep(1500);
        System.out.println("pool.size() = " + pool.size());
        System.out.println("耗时 = " + timer.interval());
    }

    @Test
    public void demoC() throws Exception {
        TimeInterval timer = DateUtil.timer();
        CopyPool pool = new CopyPool();
        for (int i = 0; i < 16; i++) {
            final int x = i;
            new Thread(() -> {
                try {
                    pool.add(x);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Thread.sleep(1500);
        System.out.println("pool.size() = " + pool.size());
        System.out.println("耗时 = " + timer.interval());
    }


}
