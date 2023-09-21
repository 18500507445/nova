package com.nova.common.trace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @description: Trace对象
 * @author: wzh
 * @date: 2022/12/20 11:16
 */
@Data
public class Trace implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * log日志配置文件也需要取出来 [%X{X-B3-TraceId}]
     */
    public static final String TRACE = "X-B3-TraceId";
    public static final String PARENT_SPAN = "X-B3-SpanId";

    /**
     * 分布式traceId
     */
    private String traceId;
    /**
     * 分布式spanId
     */
    private String spanId;
}

/**
 * trace http header 枚举
 */
@Getter
@AllArgsConstructor
enum TraceHttpHeaderEnum {

    /**
     * header发送traceId
     */
    HEADER_TRACE_ID("header_trace_id", "http请求发送traceId");

    final String code;

    final String message;
}

