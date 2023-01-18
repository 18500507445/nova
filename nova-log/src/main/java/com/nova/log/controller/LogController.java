package com.nova.log.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.result.AjaxResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 异步logController
 * @author: wzh
 * @date: 2022/11/19 17:19
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class LogController extends BaseController {

    /**
     * log
     */
    @PostMapping("log")
    public AjaxResult limit() {
        TimeInterval timer = DateUtil.timer();
        for (int i = 0; i < 100000; i++) {
            log.info("这是{}条日志！", i);
        }
        log.info("当前耗时：{}ms", timer.interval());
        return AjaxResult.success("ok");
    }

}
