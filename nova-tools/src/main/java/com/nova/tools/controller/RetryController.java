package com.nova.tools.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.github.rholder.retry.*;
import com.nova.common.core.model.result.ResResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;


/**
 * @author: wzh
 * @description: 重试
 * @date: 2023/12/05 15:56
 */
@Slf4j(topic = "RetryController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class RetryController {

    @Service
    @Slf4j
    public static class RemoteService {

        /**
         * 注解@Retryable：标记当前方法会使用重试机制 value：重试的触发机制，当遇到Exception异常的时候，会触发重试，这里的Exception你也可以写的更精确
         * maxAttempts：重试次数（包括第一次调用），默认三次
         * delay：重试的间隔时间
         * multiplier：delay时间的间隔倍数
         * maxDelay：重试次数之间的最大时间间隔，默认为0，如果小于delay的设置，则默认为30000L
         */
        @Retryable(value = {RemoteAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 2))
        public String call(Integer count) throws Exception {
            if (count == 10) {
                log.error("Remote RPC call do something... {}", DateUtil.now());
                throw new RemoteAccessException("RPC调用异常");
            }
            return "SUCCESS";
        }

        /**
         * 注解@Recover，标记方法为回调方法，传参与@Retryable的value值需一致 定义回调, 注意异常类型和方法返回值类型要与重试方法一致
         */
        @Recover
        public String recover(RemoteAccessException e) {
            log.error("Remote RPC Call fail", e);
            return "recover SUCCESS";
        }
    }

    private final RemoteService remoteService;

    /**
     * 注意事项：因为Retryable是走aop的所以直接掉用会失效，需交给spring管理才可以
     */
    @GetMapping("retry")
    public ResResult<String> retry() {
        try {
            String call = remoteService.call(10);
            return ResResult.success(call);
        } catch (Exception e) {
            log.error("RetryController.show Exception", e);
            return ResResult.failure();
        }
    }

    @GetMapping("guavaRetry")
    public ResResult<String> guavaRetry() {
        try {
            guavaRetryDemo();
            return ResResult.success();
        } catch (Exception e) {
            log.error("guavaRetry.show Exception", e);
            return ResResult.failure();
        }
    }

    /**
     * WaitStrategies.exponentialWait：指数倍数，每次重试间隔时间 = multiplier * 2^n，n为重试次数
     * WaitStrategies.fixedWait：固定等待策略，每次重试间隔固定时间，比如500毫秒
     * WaitStrategies.fibonacciWait：斐波那契策略，例如数列：1，1，2，3，5，8，13，21，34，55，89……这个数列从第3项开始 ，每一项都等于前两项之和。
     * WaitStrategies.randomWait：随机等待策略，每次重试间隔随机时间，比如0-1000毫秒
     * WaitStrategies.incrementingWait：递增等待策略，根据初始值和递增值，等待时长依次递增
     */
    public void guavaRetryDemo() throws ExecutionException, RetryException {
        // 创建重试器

        // 结果为null、""才重试
        Retryer<String> retryer = RetryerBuilder.<String>newBuilder().retryIfResult(StrUtil::isBlank)
                .retryIfException() // 有异常就重试
                .withWaitStrategy(WaitStrategies.fibonacciWait(1000, 1, TimeUnit.MINUTES))
//                .withWaitStrategy(WaitStrategies.fixedWait(500, TimeUnit.MILLISECONDS))
//                .withWaitStrategy(WaitStrategies.exponentialWait(1000, 1, TimeUnit.MINUTES))

                // 最多重试3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(10))
                .build();

        LongAdder longAdder = new LongAdder();
        // 测试重试
        String result = retryer.call(() -> {
            longAdder.increment();
            log.error("重试次数：{}，时间：{}", longAdder.longValue(), DateUtil.now());
            // 这是要重试的方法
            int i = RandomUtil.randomInt(100);
            if (i > 100) {
                log.error("success");
                return "success";
            } else {
                log.error("fail");
                return "";
            }
        });
        System.err.println("result = " + result);
    }

}
