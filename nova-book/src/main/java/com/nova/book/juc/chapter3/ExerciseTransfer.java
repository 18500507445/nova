package com.nova.book.juc.chapter3;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @description: 转账练习
 * @author: wzh
 * @date: 2023/3/26 10:14
 */
@Slf4j
public class ExerciseTransfer {

    /**
     * Random是线程安全
     */
    static Random random = new Random();

    /**
     * 随机 1~100
     *
     * @return
     */
    public static int randomAmount() {
        return random.nextInt(100) + 1;
    }

    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(100000);
        Account b = new Account(100000);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, randomAmount());
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transfer(a, randomAmount());
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // 查看转账2000次后的总金额
        log.debug("total:{}", (a.getMoney() + b.getMoney()));
    }
}


/**
 * 账户
 */
class Account {
    private int money;

    public Account(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * 转账
     * this.money和target.money是共享变量都需要保护
     * 但是synchronized加在方法上等价于synchronized（this），锁住的是不同的new出来的对象
     * 所以synchronized（Account.class）
     *
     * @param target
     * @param amount
     */
    public void transfer(Account target, int amount) {
        synchronized (Account.class) {
            if (this.money >= amount) {
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }
    }
}