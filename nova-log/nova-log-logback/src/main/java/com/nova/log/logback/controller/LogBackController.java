package com.nova.log.logback.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.http.HttpUtil;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.result.RespResult;
import com.nova.common.trace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 异步LogBackController
 * @author: wzh
 * @date: 2022/11/19 17:19
 */
@Slf4j(topic = "LogBackController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class LogBackController extends BaseController {

    /**
     * 异步log测试，性能差一倍
     */
    @RequestMapping("log")
    public RespResult<Void> limit() {
        TimeInterval timer = DateUtil.timer();
        for (int i = 0; i < 500000; i++) {
            log.info("这是{}条日志！", i);
        }
        log.info("当前耗时：{}ms", timer.interval());
        return RespResult.success();
    }

    @GetMapping("traceTest")
    public RespResult<Void> traceTest(HttpServletRequest req) {
        String traceId = req.getHeader(Trace.TRACE_ID);
        log.error("abTest-traceId ：{}", traceId);
        String httpResult = HttpUtil.createGet("http://localhost:8080/api/traceTest1").header(Trace.TRACE_ID, traceId).execute().body();
        log.error("httpResult ：{}", httpResult);
        return RespResult.success();
    }

    @GetMapping("traceTest1")
    public RespResult<Void> traceTest1(HttpServletRequest req) {
        String traceId = req.getHeader(Trace.TRACE_ID);
        log.error("abTest1-traceId ：{}", traceId);
        return RespResult.success();
    }
}