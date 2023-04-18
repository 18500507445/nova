package com.nova.limit.controller;

import com.nova.common.core.model.result.AjaxResult;
import com.nova.limit.annotation.AccessLimit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/api/")
public class LimitController {

    /**
     * redis计数器限流
     */
    @PostMapping("redisLimit")
    @AccessLimit(seconds = 5, maxCount = 5)
    public AjaxResult redisLimit() {
        return AjaxResult.success();
    }
}
