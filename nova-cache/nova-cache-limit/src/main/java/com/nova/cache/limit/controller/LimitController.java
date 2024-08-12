package com.nova.cache.limit.controller;

import com.nova.cache.limit.annotation.AccessLimit;
import com.nova.cache.limit.annotation.BucketLimit;
import com.nova.common.core.model.result.ResResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/18 19:31
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class LimitController {

    private final RedissonClient redissonClient;

    /**
     * 计数器:redis限流
     */
    @RequestMapping("/redisLimit")
    @AccessLimit(seconds = 5, maxCount = 5)
    public ResResult<Void> redisLimit() {
        return ResResult.success();
    }

    /**
     * 令牌桶限流
     * 每秒产生的令牌数，最大容量，请求的令牌数
     */
    @PostMapping("/bucket")
    @BucketLimit(rate = 1, maxCount = 5, requestNum = 1)
    public ResResult<Void> bucketLua() {
        return ResResult.success();
    }

    /**
     * redisson的令牌桶
     * 效果，1-3次请求没问题，第四次等待3秒后返回
     */
    @PostMapping("/redissonLimit")
    public ResResult<String> redissonLimit() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String methodName = stack[1].getMethodName();
        System.err.println("key = " + methodName);
        RRateLimiter redissonLimit = redissonClient.getRateLimiter(methodName);
        //5秒3令牌
        redissonLimit.trySetRate(RateType.OVERALL, 3, 5, RateIntervalUnit.SECONDS);
        //试图获取一个令牌，获取到返回true
        boolean b = redissonLimit.tryAcquire(1);
        if (!b) {
            return ResResult.failure("接口限流");
        }

        // 处理业务
        return ResResult.success(methodName);
    }

}
