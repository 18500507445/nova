package com.nova.book.juc.chapter3.section5;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description: 同步模式之保护性暂停
 * 定义：Guarded Suspension，用在一个线程等待另一个线程的执行结果
 * <p>
 * 有一个结果需要从一个线程传递到另一个线程，让他们关联同一个 GuardedObject
 * 如果有结果不断从一个线程到另一个线程那么可以使用消息队列（见生产者/消费者）
 * JDK中，join的实现、Future的实现，采用的就是此模式
 * 因为要等待另一方的结果，因此归类到同步模式
 * @author: wzh
 * @date: 2023/3/26 18:31
 */
@Slf4j(topic = "Guarded")
class Guarded {

    public static void main(String[] args) {
        //add();

        //subtraction();

        sendMsg();
    }

    /**
     * t1线程等待t2计算的结果
     */
    public static void add() {
        GuardedObjectOne guarded = new GuardedObjectOne();
        new Thread(() -> {
            log.debug("等待结果");
            Object sum = guarded.get();
            log.debug("sum：{}", sum);
        }, "t1").start();

        new Thread(() -> {
            log.debug("执行计算");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            guarded.complete(6 + 2);
        }, "t2").start();
    }

    /**
     * t2线程处理超过2秒，这时t1线程拿到结果为null
     */
    public static void subtraction() {
        GuardedObjectTwo guarded = new GuardedObjectTwo();
        new Thread(() -> {
            log.debug("等待结果");
            Object sum = guarded.get(2000);
            log.debug("sum：{}", sum);
        }, "t1").start();

        new Thread(() -> {
            log.debug("执行计算");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            guarded.complete(6 - 2);
        }, "t2").start();
    }


    /**
     * 发件人和邮递员线程
     */
    public static void sendMsg() {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Integer id : Mailboxes.getIds()) {
            new Postman(id, "" + id).start();
        }
    }
}

/**
 * 普通版
 */
class GuardedObjectOne {

    private Object response;

    public Object get() {
        synchronized (this) {
            // 条件不满足则等待
            while (null == response) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            // 条件满足，通知等待线程
            this.response = response;
            this.notifyAll();
        }
    }
}

/**
 * 加强版-超时等待，类比Thread.join()方法
 */
class GuardedObjectTwo {

    private int id;

    public GuardedObjectTwo() {

    }

    public GuardedObjectTwo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private Object response;

    public Object get(long timeout) {
        synchronized (this) {
            //记录一个开始时间
            final TimeInterval timer = DateUtil.timer();
            final long begin = timer.interval();
            //经过时间
            long passed = 0;
            // 条件不满足则等待
            while (null == response) {
                //当前轮次应该等待的时间
                long waitTime = timeout - passed;
                //经历的时间超过了最大等待时间
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passed = timer.interval() - begin;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            // 条件满足，通知等待线程
            this.response = response;
            this.notifyAll();
        }
    }
}

/**
 * 信箱-包装版
 */
class Mailboxes {

    private static final Map<Integer, GuardedObjectTwo> boxes = new Hashtable<>();

    private static int id = 1;

    /**
     * 产生唯一 id
     *
     * @return
     */
    private static synchronized int generateId() {
        return id++;
    }

    public static GuardedObjectTwo getGuardedObject(int id) {
        return boxes.remove(id);
    }

    public static GuardedObjectTwo createGuardedObject() {
        GuardedObjectTwo go = new GuardedObjectTwo(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}

@Slf4j(topic = "邮递员")
class Postman extends Thread {
    private final int id;
    private final String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObjectTwo guardedObject = Mailboxes.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}", id, mail);
        guardedObject.complete(mail);
    }
}

@Slf4j(topic = "发件人")
class People extends Thread {
    @Override
    public void run() {
        // 收信
        GuardedObjectTwo guardedObject = Mailboxes.createGuardedObject();
        log.debug("开始收信 id:{}", guardedObject.getId());
        Object mail = guardedObject.get(5000);
        log.debug("收到信 id:{}, 内容:{}", guardedObject.getId(), mail);
    }
}