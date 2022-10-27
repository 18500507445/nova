package com.nova.tools.utils.guava.ratelimit;

import cn.hutool.core.thread.ThreadUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.nova.common.annotation.ExtRateLimiter;
import org.junit.jupiter.api.Test;

/**
 * @Description: 令牌桶算法（单台服务器）
 * 令牌桶跟漏桶算法有点不一样，令牌桶算法也有一个大桶，桶中装的都是令牌，
 * 有一个固定的“人”在不停的往桶中放令牌，每个请求来的时候都要从桶中拿到令牌，要不然就无法进行请求操作
 * @Author: wangzehui
 * @Date: 2022/10/13 19:40
 */
public class RateLimiterTest {

    /**
     * 每秒5个令牌
     */
    public static final RateLimiter RATELIMITER = RateLimiter.create(5);

    public static final int LIMIT_TIME = 1000;

    @Test
    public void test() {
        while (true) {
            System.out.println("time:" + RATELIMITER.acquire() + "s");
            //线程休眠，给足够的时间产生令牌
            ThreadUtil.safeSleep(LIMIT_TIME);
        }
    }

    @Test
    public void testA() {
        if (!RATELIMITER.tryAcquire()) {
            System.out.println("限流中......");
        } else {
            System.out.println("未限流，继续执行业务");
        }
    }

    @Test
    @ExtRateLimiter(value = 5, timeOut = LIMIT_TIME)
    public void testAop() {
        System.out.println("测试接口限流");
    }


}
