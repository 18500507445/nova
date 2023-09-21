package com.nova.common.trace;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.slf4j.MDC;
import org.springframework.core.NamedThreadLocal;

/**
 * @description: traceId和spanId工具类
 * @author: wzh
 * @date: 2022/12/20 11:16
 */
public class TraceHelper {

    public static final ThreadLocal<Trace> CURRENT_SPAN = new NamedThreadLocal<>("TraceId Context");

    public static String genTraceId() {
        return Convert.toStr(new SnowflakeGenerator().next());
    }

    public static String genSpanId() {
        return SecureUtil.md5(UUID.randomUUID().toString()).substring(2, 16);
    }

    public static String getTraceId() {
        return MDC.get(Trace.TRACE);
    }

    /**
     * 设置traceId
     */
    public static void setCurrentTrace(String traceId) {
        if (StrUtil.isBlank(traceId)) {
            traceId = genTraceId();
        }
        Trace trace = new Trace();
        trace.setTraceId(traceId);
        trace.setSpanId(genSpanId());
        MDC.put(Trace.TRACE, trace.getTraceId());
        MDC.put(Trace.PARENT_SPAN, trace.getSpanId());
        CURRENT_SPAN.set(trace);
    }

    /**
     * 获取traceId
     */
    public static Trace getCurrentTrace() {
        Trace trace = CURRENT_SPAN.get();
        if (trace == null) {
            trace = new Trace();
            trace.setTraceId(genTraceId());
            trace.setSpanId(genSpanId());
            MDC.put(Trace.TRACE, trace.getTraceId());
        } else {
            // spanId每次不一样，重新生成
            trace.setSpanId(genSpanId());
        }
        MDC.put(Trace.PARENT_SPAN, trace.getSpanId());
        CURRENT_SPAN.set(trace);
        return trace;
    }

    /**
     * 清空traceId
     */
    public static void clearCurrentTrace() {
        CURRENT_SPAN.set(null);
        CURRENT_SPAN.remove();
    }
}