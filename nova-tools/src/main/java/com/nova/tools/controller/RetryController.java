package com.nova.tools.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.rholder.retry.*;
import com.nova.common.core.model.result.avic.ResultVO;
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
 * @description 重试
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
         * @Retryable：标记当前方法会使用重试机制 value：重试的触发机制，当遇到Exception异常的时候，会触发重试，这里的Exception你也可以写的更精确
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
         * @Recover，标记方法为回调方法，传参与@Retryable的value值需一致 定义回调, 注意异常类型和方法返回值类型要与重试方法一致
         */
        @Recover
        public String recover(RemoteAccessException e) {
            log.error("Remote RPC Call fail", e);
            return "recover SUCCESS";
        }
    }

    private final RemoteService remoteService;

    /**
     * 注意事项：因为Retryable是走aop的所以直接掉会失效
     */
    @GetMapping("retry")
    public ResultVO<String> retry() {
        try {
            String call = remoteService.call(10);
            return ResultVO.success(call);
        } catch (Exception e) {
            log.error("RetryController.show Exception", e);
            return ResultVO.failure();
        }
    }

    @GetMapping("guavaRetry")
    public ResultVO<String> guavaRetry() {
        try {
            guavaRetryDemo();
            return ResultVO.success();
        } catch (Exception e) {
            log.error("guavaRetry.show Exception", e);
            return ResultVO.failure();
        }
    }

    public void guavaRetryDemo() throws ExecutionException, RetryException {
        // 创建重试器
        Retryer<String> retryer = RetryerBuilder.<String>newBuilder()
                .retryIfResult(StrUtil::isBlank) // 结果为null、""才重试
                .retryIfException() // 有异常就重试
                .withWaitStrategy(WaitStrategies.fixedWait(500, TimeUnit.MILLISECONDS)) // 每次重试间隔500毫秒
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 最多重试3次
                .build();

        LongAdder longAdder = new LongAdder();
        // 测试重试
        String result = retryer.call(() -> {
            longAdder.increment();
            log.error("重试次数：{}", longAdder.longValue());
            // 这是要重试的方法
            if (Math.random() < 0.5) {
                return "success";
            } else {
                return "";
            }
        });
        System.err.println(result);
    }

}
