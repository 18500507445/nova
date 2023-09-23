package com.nova.common.trace;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.common.context.RequestWrapper;
import com.nova.common.context.RequestParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
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
@Slf4j
public class TraceWebFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        try {
            long start = System.currentTimeMillis();
            HttpServletRequest request = (HttpServletRequest) req;
            String traceId = request.getHeader(TraceHttpHeaderEnum.HEADER_TRACE_ID.getCode());
            //正常启动单服务可能拿不到，需要生成一个，如果网关进行设置了，那么直接透传进来
            if (StrUtil.isNotEmpty(traceId)) {
                TraceHelper.setCurrentTrace(traceId);
            } else {
                TraceHelper.getCurrentTrace();
            }
            RequestWrapper requestWrapper = printAccessLog(request);
            String currentTraceId = TraceHelper.getCurrentTrace().getTraceId();
            //todo 正常逻辑应该网关进行处理放入header进行透传
            if (null == requestWrapper) {
                requestWrapper = new RequestWrapper(request);
                requestWrapper.addHeader(TraceHttpHeaderEnum.HEADER_TRACE_ID.getCode(), currentTraceId);
            }
            log.info("trace web filter-traceId:{}", currentTraceId);
            filterChain.doFilter(requestWrapper, resp);
            //MDC放入spanId
            MDC.put(Trace.PARENT_SPAN, TraceHelper.genSpanId());
            log.error("当前请求总耗时：{} ms", System.currentTimeMillis() - start);
        } finally {
            TraceHelper.clearCurrentTrace();
        }
    }

    /**
     * 打印访问日志
     */
    @SuppressWarnings({"unchecked"})
    private RequestWrapper printAccessLog(HttpServletRequest request) throws IOException {
        RequestWrapper requestWrapper;
        String requestUrl = request.getRequestURI();
        SortedMap<String, Object> paramResult = new TreeMap<>(RequestParamsUtil.getUrlParams(request));
        try {
            if (!StrUtil.equals(HttpMethod.GET.name(), request.getMethod())) {
                String contentType = request.getContentType();
                if (StrUtil.containsIgnoreCase(contentType, "json")) {
                    requestWrapper = new RequestWrapper(request);
                    paramResult.putAll(JSONObject.parseObject(getBody(requestWrapper), Map.class));
                    return requestWrapper;
                } else if (StrUtil.containsIgnoreCase(contentType, "form")) {
                    paramResult.putAll(RequestParamsUtil.getFormParams(request));
                }
            }
        } finally {
            log.info("请求apiName：{}，方式：{}，body：{}", requestUrl, request.getMethod(), JSONObject.toJSONString(paramResult));
        }
        return null;
    }

    public static String getBody(HttpServletRequest request) {
        try {
            ServletInputStream in = request.getInputStream();
            String body;
            body = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
            return body;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }
}