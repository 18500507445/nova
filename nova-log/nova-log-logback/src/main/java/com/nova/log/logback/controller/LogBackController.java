package com.nova.log.logback.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.http.HttpUtil;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.result.RespResult;
import com.nova.common.trace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping(value = "dynamicLog", name = "动态修改日志级别")
    public RespResult<Void> dynamicLog(@RequestParam String logLevel) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("ROOT").setLevel(Level.INFO);
        Logger logger = loggerContext.getLogger("com.nova.log.logback");
        logger.setLevel(Level.valueOf(logLevel));


        System.err.println("level = " + logger.getLevel().toString());
        log.debug("dynamicLog ：{}", DateUtil.now());
        log.info("dynamicLog ：{}", DateUtil.now());
        log.warn("dynamicLog ：{}", DateUtil.now());
        log.error("dynamicLog ：{}", DateUtil.now());
        return RespResult.success();
    }

}
