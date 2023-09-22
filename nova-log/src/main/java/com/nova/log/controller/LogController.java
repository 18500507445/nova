package com.nova.log.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.result.AjaxResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 异步logController
 * @author: wzh
 * @date: 2022/11/19 17:19
 */
@Slf4j(topic = "LogController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class LogController extends BaseController {

    /**
     * 异步log测试，性能差一倍
     */
    @RequestMapping("log")
    public AjaxResult limit() {
        TimeInterval timer = DateUtil.timer();
        for (int i = 0; i < 500000; i++) {
            log.info("这是{}条日志！", i);
        }
        log.info("当前耗时：{}ms", timer.interval());
        return AjaxResult.success("ok");
    }

}
