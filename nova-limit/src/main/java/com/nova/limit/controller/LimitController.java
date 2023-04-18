package com.nova.limit.controller;

import com.nova.common.core.model.result.AjaxResult;
import com.nova.limit.annotation.AccessLimit;
import com.nova.limit.annotation.BucketLimit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    /**
     * 令牌桶
     * 可根据自己的场景自定义 拦截
     * 1. 拦截器拦截所有接口并且限流
     * 2. 拦截器根据url可配置化限流，针对接口的限流方式。拦截器获取到uri后再读取配置中接口的参数创建
     * bucket对象，然后调用bucket，bucket实例的key理应是接口uri
     */
    @PostMapping("bucket")
    @BucketLimit(seconds = 1, maxCount = 5)
    public AjaxResult bucket() {
        log.info("成功进入");
        return AjaxResult.success();
    }

}
