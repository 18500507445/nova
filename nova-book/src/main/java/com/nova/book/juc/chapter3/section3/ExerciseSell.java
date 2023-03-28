package com.nova.book.juc.chapter3.section3;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * @description: 售票练习
 * @author: wzh
 * @date: 2023/3/26 10:02
 */
@Slf4j(topic = "ExerciseSell")
public class ExerciseSell {

    public static void main(String[] args) throws InterruptedException {
        // 模拟多人买票
        TicketWindow window = new TicketWindow(1000);
        // 所有线程的集合
        List<Thread> threadList = new ArrayList<>();
        // 卖出的票数统计，问啥实现用Vector？答：Vector的add是synchronized保证线程安全
        List<Integer> amountList = new Vector<>();
        for (int i = 0; i < 2000; i++) {
            Thread thread = new Thread(() -> {
                // 买票
                int amount = window.sell(random(5));
                // 统计买票数
                amountList.add(amount);
            });
            threadList.add(thread);
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }

        // 统计卖出的票数和剩余票数
        log.debug("余票：{}", window.getCount());
        log.debug("卖出的票数：{}", amountList.stream().mapToInt(i -> i).sum());
    }

    /**
     * Random 为线程安全
     */
    static Random random = new Random();

    /**
     * 随机 1~5
     *
     * @param amount
     * @return
     */
    public static int random(int amount) {
        return random.nextInt(amount) + 1;
    }
}


/**
 * 售票窗口
 */
class TicketWindow {
    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    /**
     * 获取余票数量
     *
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * 售票
     * 共享变量读写操作的临界区需要加synchronized保证线程安全
     *
     * @param amount
     * @return
     */
    public synchronized int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}