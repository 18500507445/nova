package com.nova.tools.demo;

import cn.hutool.http.HttpUtil;
import com.nova.common.core.model.result.RespResult;
import com.starter.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: wzh
 * @description 测试Controller
 * @date: 2023/05/26 12:09
 */
@Slf4j(topic = "DemoController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class DemoController {

    private final RedisService redisService;

    /**
     * apache ab测试
     * 查看版本：apachectl -v
     * 测试连接：ab -n 请求数 -c 并发数 URL
     * ab工具常用参数：
     * -n：总共的请求执行数，缺省是1；
     * -c： 并发数，缺省是1；
     * -t：测试所进行的总时间，秒为单位，缺省50000s
     * -p：POST时的数据文件
     * -w: 以HTML表的格式输出结果
     */
    @GetMapping("abTest")
    public RespResult<Void> abTest(HttpServletRequest req, HttpServletResponse resp) {
        String httpResult = HttpUtil.createGet("http://localhost:8080/api/abTest1").execute().body();
        String traceId = req.getHeader("header_trace_id");
        String headerTraceId = resp.getHeader("header_trace_id");
        System.err.println("abTest-traceId = " + traceId);
        System.err.println("abTest-headerTraceId = " + headerTraceId);
        System.err.println("httpResult = " + httpResult);
        return RespResult.success();
    }

    @GetMapping("abTest1")
    public RespResult<Void> abTest1(HttpServletRequest req, HttpServletResponse resp) {
        String traceId = req.getHeader("header_trace_id");
        String headerTraceId = resp.getHeader("header_trace_id");
        System.err.println("abTest1-traceId = " + traceId);
        System.err.println("abTest1-headerTraceId = " + headerTraceId);
        return RespResult.success();
    }
}
