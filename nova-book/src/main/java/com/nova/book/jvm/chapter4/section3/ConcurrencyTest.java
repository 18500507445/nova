package com.nova.book.jvm.chapter4.section3;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 有序性
 * @author: wzh
 * @date: 2023/3/22 22:46
 */
public class ConcurrencyTest {

    static int num;
    static boolean ready = false;

    /**
     * 情况1：线程1 先执行，这时 ready = false，所以进入 else 分支结果为 1
     * 情况2：线程2 先执行 num = 2，但没来得及执行 ready = true，线程1 执行，还是进入 else 分支，结果为1
     * 情况3：线程2 执行到 ready = true，线程1 执行，这回进入 if 分支，结果为 4（因为 num 已经执行过了）
     * 情况4（指令重排）：线程2 执行 ready = true，切换到线程1，进入 if 分支，相加为 0，再切回线程2 执行num = 2，解决办法使用volatile关键字或者使用原子性包装类
     *
     * @param args
     */
    public static void main(String[] args) {
        int i = 1;
        while (true) {
            i++;
            AtomicInteger result = new AtomicInteger();

            Thread t1 = new Thread(() -> {
                if (ready) {
                    result.set(num + num);
                } else {
                    result.set(1);
                }
            });
            t1.start();

            Thread t2 = new Thread(() -> {
                num = 2;
                ready = true;
            });
            t2.start();

            System.out.println("第" + i + "次调用，result：" + result);
        }
    }

}
