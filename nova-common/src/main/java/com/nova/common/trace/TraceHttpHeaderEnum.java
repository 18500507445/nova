package com.nova.common.trace;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: trace http header 枚举
 * @author: wangzehui
 * @date: 2022/12/20 11:16
 */
@Getter
@AllArgsConstructor
public enum TraceHttpHeaderEnum {

    /**
     * header发送traceId
     */
    HEADER_TRACE_ID("header_trace_id", "http请求发送traceId");

    String code;

    String message;
}
