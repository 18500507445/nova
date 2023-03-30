package com.nova.book.juc.chapter7.section2;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @description: 面试题：点餐
 * <p>
 * 固定大小线程池会有饥饿现象
 * 两个工人是同一个线程池中的两个线，他们要做的事情是：为客人点餐和到后厨做菜，这是两个阶段的工作
 * （1）客人点餐：必须先点完餐，等菜做好，上菜，在此期间处理点餐的工人必须等待
 * （2）后厨做菜：没啥说的，做就是了
 * 比如工人A处理了点餐任务，接下来它要等着工人B把菜做好，然后上菜，他俩也配合的蛮好
 * 但现在同时来了两个客人，这个时候工人A和工人B都去处理点餐了，这时没人做饭了，饥饿
 * @author: wzh
 * @date: 2023/3/30 16:44
 */
@Slf4j(topic = "TopicStarvation")
public class TopicStarvation {

    static final List<String> MENU = Arrays.asList("地三鲜", "宫保鸡丁", "辣子鸡丁", "烤鸡翅");
    static Random RANDOM = new Random();

    static String cooking() {
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }

    /**
     * 解决方法可以增加线程池的大小，不过不是根本解决方案，还是前面提到的，不同的任务类型，采用不同的线程
     * 池
     *
     * @param args
     */
    public static void main(String[] args) {
        ExecutorService waiterPool = Executors.newFixedThreadPool(1);
        ExecutorService cookPool = Executors.newFixedThreadPool(1);

        waiterPool.execute(() -> {
            log.debug("处理点餐...");
            Future<String> f = cookPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        waiterPool.execute(() -> {
            log.debug("处理点餐...");
            Future<String> f = cookPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
