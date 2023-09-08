package com.nova.common.trace;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;

/**
 * @author: wzh
 * @description 分部署链路工具
 * @date: 2023/08/31 22:15
 */
public class TracerUtils {

    private TracerUtils() {

    }

    /**
     * 获得链路追踪编号，直接返回 SkyWalking 的 TraceId。
     *
     * @return 链路追踪编号
     */
    public static String getTraceId() {
        return TraceContext.traceId();
    }
}
