package com.nova.common.trace;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @description: web端链路过滤器处理(设置traceId, spanId)
 * @author: wzh
 * @date: 2022/12/20 11:16
 */
@Component
@Slf4j(topic = "TraceFilter")
public class TraceFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        try {
            long start = System.currentTimeMillis();
            HttpServletRequest request = (HttpServletRequest) req;
            String traceId = request.getHeader(Trace.TRACE_ID);
            //正常启动单服务可能拿不到，需要生成一个，如果网关进行设置了直接放入Trace对象
            if (StrUtil.isNotEmpty(traceId)) {
                traceId = TraceContext.setCurrentTrace(traceId).getTraceId();
            } else {
                traceId = TraceContext.getCurrentTrace().getTraceId();
            }
            RequestWrapper requestWrapper = printAccessLog(request);

            //todo 正常逻辑应该网关进行处理放入header进行透传
            if (StrUtil.isNotBlank(traceId) && null != requestWrapper) {
                requestWrapper.addHeader(Trace.TRACE_ID, traceId);
            }

            filterChain.doFilter(requestWrapper != null ? requestWrapper : request, resp);
            log.info("当前请求总耗时：{} ms", System.currentTimeMillis() - start);
        } finally {
            TraceContext.removeTrace();
        }
    }

    /**
     * 打印访问日志
     * 经过测试，get方式也可以传from表单（接口用实体类接收，不用添加@RequestBody）和body-json
     * 因为无法控制编程规范问题，所以都需要打印
     */
    @SuppressWarnings({"unchecked"})
    private RequestWrapper printAccessLog(HttpServletRequest request) throws IOException {
        RequestWrapper requestWrapper = null;
        String requestUrl = request.getRequestURI();
        SortedMap<String, Object> paramResult = new TreeMap<>(RequestParamsUtil.getUrlParams(request));
        String contentType = request.getContentType();
        try {
            //get请求，少判断一次，直接return
            if (StrUtil.isNotBlank(contentType)) {
                //json
                if (StrUtil.containsIgnoreCase(contentType, "json")) {
                    String body = getBody(requestWrapper = new RequestWrapper(request));
                    if (StrUtil.isNotBlank(body)) {
                        paramResult.putAll(JSONObject.parseObject(body, Map.class));
                    }
                }
                //from
                if (StrUtil.containsIgnoreCase(contentType, "form")) {
                    paramResult.putAll(RequestParamsUtil.getFormParams(request));
                }
            }
        } finally {
            log.info("请求apiName：{}，方式：{}，body：{}", requestUrl, request.getMethod(), JSONObject.toJSONString(paramResult));
        }
        return requestWrapper;
    }

    public static String getBody(HttpServletRequest request) {
        String body = "";
        try {
            ServletInputStream in = request.getInputStream();
            body = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return body;
    }
}