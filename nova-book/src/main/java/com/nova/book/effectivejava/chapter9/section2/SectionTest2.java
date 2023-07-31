package com.nova.book.effectivejava.chapter9.section2;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.nova.common.utils.thread.Threads;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

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
                Threads.sleep(1000);
                return super.add(s);
            }
        });

        for (int i = 0; i < 16; i++) {
            final int x = i;
            new Thread(() -> pool.add(x)).start();
        }
        TimeUnit.SECONDS.sleep(4);
        System.err.println("pool.size() = " + pool.size());
    }

    @Test
    public void demoB() throws Exception {
        TimeInterval timer = DateUtil.timer();
        Pool pool = new Pool(new LinkedList<>());
        for (int i = 0; i < 16; i++) {
            final int x = i;
            new Thread(() -> {
                pool.add(x);
                Threads.sleep(2000);
            }).start();
        }
        Threads.sleep(1500);
        System.err.println("pool.size() = " + pool.size());
        System.err.println("耗时 = " + timer.interval());
    }

    @Test
    public void demoC() throws Exception {
        TimeInterval timer = DateUtil.timer();
        CopyPool pool = new CopyPool();
        for (int i = 0; i < 16; i++) {
            final int x = i;
            new Thread(() -> {
                pool.add(x);
                Threads.sleep(2000);
            }).start();
        }
        Threads.sleep(1500);
        System.err.println("pool.size() = " + pool.size());
        System.err.println("耗时 = " + timer.interval());
    }


}
