package com.nova.book.juc.chapter5;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: 面试题：转账
 * @author: wzh
 * @date: 2023/3/29 13:41
 */
@Slf4j(topic = "TopicAccount")
class TopicAccount {

    /**
     * 无锁效率更高
     *
     * @param args
     */
    public static void main(String[] args) {
        Account accountCas = new AccountCas(10000);
        Account.demo(accountCas);

        Account accountUnsafe = new AccountUnsafe(10000);
        Account.demo(accountUnsafe);
    }
}

interface Account {

    /**
     * 获取余额
     */
    Integer getBalance();

    /**
     * 取款
     * @param amount
     */
    void withdraw(Integer amount);

    /**
     * 方法内会启动1000个线程，每个线程做-10元的操作
     * 如果初始余额为10000那么正确的结果应当是0
     *
     * @param account
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        final TimeInterval timer = DateUtil.timer();
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                Console.log("异常：", e);
            }
        });
        System.err.println("余额: " + account.getBalance() + "， 耗时: " + timer.interval() + " ms");
    }

}

class AccountUnsafe implements Account {

    private Integer balance;

    public AccountUnsafe(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {
        synchronized (this) {
            return this.balance;
        }
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this) {
            this.balance -= amount;
        }
    }
}

class AccountCas implements Account {

    private final AtomicInteger balance;

    private AtomicReference<BigDecimal> bigDecimal;

    public AccountCas(int balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        balance.getAndAdd(-1 * amount);
    }
}