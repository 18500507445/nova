package com.nova.cache.controller;

import com.nova.cache.caffeine.CaffeineCacheUtil;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/11/19 17:19
 */
@Slf4j
@RestController
@RequestMapping("/api/")
public class DemoController extends BaseController {

    @Resource
    private CaffeineCacheUtil caffeineCacheUtil;

    /**
     * caffeineCache
     */
    @PostMapping("caffeineCache")
    @ResponseBody
    public AjaxResult caffeineCache() {
        String cacheName = "caffeine";
        String key = "nova-cache";
        caffeineCacheUtil.putCache(cacheName, key, "1");

        Object cache = caffeineCacheUtil.getCache(cacheName, key);
        return AjaxResult.success(cache);
    }

}
