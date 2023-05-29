package com.nova.lock.controller;

import cn.hutool.json.JSONUtil;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.business.ValidatorReqDTO;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.lock.annotation.Lock;
import com.nova.lock.core.RedissonLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: wzh
 * @date: 2022/11/19 17:19
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class DemoController extends BaseController {

    private final RedissonLock redissonLock;

    /**
     * redisson
     */
    @Lock(value = "redisson", expireSeconds = 60)
    @PostMapping("redisson")
    public AjaxResult redisson(ValidatorReqDTO reqDto) {
        return AjaxResult.success(reqDto);
    }

    @PostMapping("offer")
    public AjaxResult offer(ValidatorReqDTO reqDto) {
        return AjaxResult.success(redissonLock.bQueueOffer(reqDto.getName(), JSONUtil.toJsonStr(reqDto), 500, TimeUnit.MILLISECONDS));
    }

    @PostMapping("poll")
    public AjaxResult poll(ValidatorReqDTO reqDto) {
        String json = redissonLock.bQueuePoll(reqDto.getName(), 500, TimeUnit.MILLISECONDS, String.class);
        return AjaxResult.success("success", json);
    }

    @PostMapping("size")
    public AjaxResult size(ValidatorReqDTO reqDto) {
        return AjaxResult.success(redissonLock.bQueueSize(reqDto.getName()));
    }
}
