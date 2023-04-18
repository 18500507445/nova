package com.nova.limit;

import com.nova.limit.bucket.Bucket;
import com.nova.limit.bucket.LocalTokenBucket;
import com.nova.limit.bucket.TokenBucket;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@SpringBootTest
class LimitApplicationTests {

    // 请求总数
    public static int clientTotal = 20;

    // 同时并发执行的线程数
    public static int threadTotal = 20;

    public static void main(String[] strings) throws InterruptedException {
        Bucket bucket = new Bucket();
        bucket.setBucketMaxCapacity(10);
        bucket.setKey("/test");
        bucket.setPutSpeed(1);
        TokenBucket tokenBucket = new LocalTokenBucket();
        tokenBucket.createTokenBucket(bucket);
        ExecutorService executorService = Executors.newFixedThreadPool(threadTotal);
        //信号量，此处用于控制并发的线程数
        final Semaphore semaphore = new Semaphore(threadTotal);
        //闭锁，可实现计数器递减
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    // 执行此方法用于获取执行许可，当总计未释放的许可数不超过200时，
                    // 允许通行，否则线程阻塞等待，直到获取到许可。
                    semaphore.acquire();
                    System.out.println(tokenBucket.getToken("/test"));
                    // 释放许可
                    semaphore.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        // 线程阻塞，直到闭锁值为0时，阻塞才释放，继续往下执行
        countDownLatch.await();
        executorService.shutdown();
    }

}
