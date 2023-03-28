package com.nova.book.juc.chapter3.section5;

import com.nova.common.utils.thread.Threads;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @description: wait notify的正确姿势
 * @author: wzh
 * @date: 2023/3/26 14:01
 */
@Slf4j(topic = "Step")
class CorrectPostureStep {

    static final Object room = new Object();
    /**
     * 有没有烟
     */
    static boolean hasCigarette = false;

    static boolean hasTakeout = false;

    /**
     * 其它干活的线程，都要一直阻塞，效率太低
     * 小男线程必须睡足2后才能醒来，就算烟提前送到，也无法立刻醒来
     * 加了synchronized (room) 后，就好比小男在里面反锁了门睡觉，烟根本没法送进门，main没加
     * synchronized就好像main线程是翻窗户进来的
     * 解决方法，使用wait - notify
     */
    @Test
    public void one() {
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    Threads.sleep(2000);
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开始干活了");
                }
            }
        }, "小男").start();

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                synchronized (room) {
                    log.debug("可以开始干活了");
                }
            }, "打工人").start();
        }

        Threads.sleep(1000);
        new Thread(() -> {
            // 这里能不能加 synchronized (room)？答：不能
            hasCigarette = true;
            log.debug("烟到了！");
        }, "送烟员").start();
    }

    /**
     * 解决了其它干活的线程阻塞的问题，但如果有其它线程也在等待条件呢？
     *
     * @throws InterruptedException
     */
    @Test
    public void two() throws InterruptedException {
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开始干活了");
                }
            }
        }, "小男").start();

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                synchronized (room) {
                    log.debug("可以开始干活了");
                }
            }, "打工人").start();
        }

        Threads.sleep(1000);
        new Thread(() -> {
            synchronized (room) {
                hasCigarette = true;
                log.debug("烟到了！");
                room.notify();
            }
        }, "送烟员").start();
    }

    /**
     * 用notifyAll仅解决某个线程的唤醒问题，但使用if + wait判断仅有一次机会，一旦条件不成立，就没有重新判断的机会了
     *
     * @throws InterruptedException
     */
    @Test
    public void three() throws InterruptedException {
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开始干活了");
                } else {
                    log.debug("没干成活...");
                }
            }
        }, "小男").start();

        new Thread(() -> {
            synchronized (room) {
                log.debug("有饭吗？[{}]", hasTakeout);
                if (!hasTakeout) {
                    log.debug("没外卖，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有饭吗？[{}]", hasTakeout);
                if (hasTakeout) {
                    log.debug("可以开始干活了");
                } else {
                    log.debug("没干成活...");
                }
            }
        }, "小女").start();

        Threads.sleep(1000);
        new Thread(() -> {
            synchronized (room) {
                hasTakeout = true;
                log.debug("外卖到了！");
                //全部唤醒
                room.notifyAll();
            }
        }, "外卖员").start();
    }

    /**
     * 完美方案
     * 用while + wait，当条件不成立，再次wait
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开始干活了");
                } else {
                    log.debug("没干成活...");
                }
            }
        }, "小男").start();

        new Thread(() -> {
            synchronized (room) {
                log.debug("有饭吗？[{}]", hasTakeout);
                if (!hasTakeout) {
                    log.debug("没外卖，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有饭吗？[{}]", hasTakeout);
                if (hasTakeout) {
                    log.debug("可以开始干活了");
                } else {
                    log.debug("没干成活...");
                }
            }
        }, "小女").start();

        Threads.sleep(1000);
        new Thread(() -> {
            synchronized (room) {
                hasTakeout = true;
                log.debug("外卖到了！");
                //全部唤醒
                room.notifyAll();
            }
        }, "外卖员").start();
    }
}
