package com.nova.tools.demo.guava;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.jupiter.api.Test;

/**
 * @Description: 令牌桶算法
 * 令牌桶跟漏桶算法有点不一样，令牌桶算法也有一个大桶，桶中装的都是令牌，
 * 有一个固定的“人”在不停的往桶中放令牌，每个请求来的时候都要从桶中拿到令牌，要不然就无法进行请求操作
 * @Author: wangzehui
 * @Date: 2022/10/13 19:40
 */
public class RateLimiterTest {

    @Test
    public void test() throws InterruptedException {

        //每秒5个令牌
        RateLimiter rateLimiter = RateLimiter.create(5);
        while (true) {
            System.out.println("time:" + rateLimiter.acquire() + "s");
            //线程休眠，给足够的时间产生令牌
            Thread.sleep(1000);
        }

    }

}
