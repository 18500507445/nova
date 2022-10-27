package com.nova.tools.utils.vavr;

import io.vavr.concurrent.Future;

import java.util.concurrent.TimeUnit;

/**
 * @Description: Vavr通过Future简化了线程的使用方式，不用再像Java定义任务，创建线程，再执行，直接创建一个Future对象即可。
 * Future提供的所有操作都是非阻塞的，其底层的ExecutorService用于执行异步处理程序
 * @Author: wangzehui
 * @Date: 2022/10/13 16:30
 */
public class FutureDemo {

    public static void main(String[] args) {

        System.out.println("当前线程名称：" + Thread.currentThread().getName());
        Integer result = Future.of(() -> {
            System.out.println("future线程名称：" + Thread.currentThread().getName());
            Thread.sleep(2000);
            return 100;
        })
                .map(i -> i * 10)
                .await(3000, TimeUnit.MILLISECONDS)
                .get();
        System.out.println(result);
        // 当前线程名称：main
        // future线程名称：ForkJoinPool.commonPool-worker-3
        // 过三秒输出，1000
    }
}
