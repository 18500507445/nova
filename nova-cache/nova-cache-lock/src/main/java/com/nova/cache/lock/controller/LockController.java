package com.nova.cache.lock.controller;

import cn.hutool.json.JSONUtil;
import com.nova.cache.lock.annotation.Lock;
import com.nova.cache.lock.core.RedissonLock;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.business.ValidatorReqDTO;
import com.nova.common.core.model.result.ResResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @description: 缓存redisson测试
 * @author: wzh
 * @date: 2022/11/19 17:19
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class LockController extends BaseController {

    private final RedissonLock redissonLock;

    /**
     * redisson
     */
    @Lock(value = "redisson", expireSeconds = 60)
    @PostMapping("redisson")
    public ResResult<ValidatorReqDTO> redisson(ValidatorReqDTO reqDto) {
        return ResResult.success(reqDto);
    }

    //分布式队列BQueue，阻塞
    @PostMapping("offer")
    public ResResult<Boolean> offer(ValidatorReqDTO reqDto) {
        return ResResult.success(redissonLock.bQueueOffer(reqDto.getName(), JSONUtil.toJsonStr(reqDto), 500, TimeUnit.MILLISECONDS));
    }

    @PostMapping("poll")
    public ResResult<String> poll(ValidatorReqDTO reqDto) {
        String json = redissonLock.bQueuePoll(reqDto.getName(), 500, TimeUnit.MILLISECONDS, String.class);
        return ResResult.success(json);
    }

    @PostMapping("size")
    public ResResult<Integer> size(ValidatorReqDTO reqDto) {
        return ResResult.success(redissonLock.bQueueSize(reqDto.getName()));
    }

    @GetMapping("redis")
    public ResResult<String> redis() {
        String key = "redis";
        redissonLock.setCacheObject(key, key, Duration.ofSeconds(1));
        String value = redissonLock.getCacheObject(key);
        return ResResult.success(value);
    }

    /**
     * @description: 订阅不会进行删除消息，每次调用越来越多
     */
    @GetMapping("publish")
    public ResResult<Void> publish() {
        String key = "redis";
        String value = "redis";
        redissonLock.publish(key, value, consumer -> {
            System.err.println("发布通道 => " + key + ", 发送值 => " + value);
        });

        redissonLock.subscribe(key, String.class, msg -> {
            System.err.println("订阅通道 => " + key + ", 接收值 => " + msg);
        });
        return ResResult.success();
    }

}
