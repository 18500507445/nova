package com.nova.common.trace;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

/**
 * @description: traceId和spanId工具类
 * @author: wzh
 * @date: 2022/12/20 11:16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TraceContext {

    /**
     * trace对象上下文，支持父子线程之间的数据传递
     */
    public static final ThreadLocal<Trace> TRACE_CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 禁止 new SnowflakeGenerator().next()生成的id有重复的
     */
    public static String genTraceId() {
        return IdUtil.getSnowflake().nextIdStr();
    }

    public static String genSpanId() {
        return SecureUtil.md5(UUID.randomUUID().toString()).substring(2, 16);
    }

    /**
     * 从MDC获取traceId
     */
    public static String getTraceId() {
        return MDC.get(Trace.TRACE_ID);
    }

    /**
     * 设置traceId
     */
    public static Trace setCurrentTrace(String traceId) {
        if (StrUtil.isBlank(traceId)) {
            traceId = genTraceId();
        }
        Trace trace = new Trace();
        trace.setTraceId(traceId);
        trace.setSpanId(genSpanId());
        MDC.put(Trace.TRACE_ID, trace.getTraceId());
        MDC.put(Trace.SPAN_ID, trace.getSpanId());
        TRACE_CONTEXT.set(trace);
        return trace;
    }

    /**
     * 获取traceId
     */
    public static Trace getCurrentTrace() {
        Trace trace = TRACE_CONTEXT.get();
        if (trace == null) {
            //如果为空，从新设置一次
            trace = setCurrentTrace("");
        }
        return trace;
    }

    /**
     * 清空trace对象
     */
    public static void removeTrace() {
        MDC.clear();
        TRACE_CONTEXT.remove();
    }
}