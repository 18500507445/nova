package com.nova.common.trace;

import lombok.Data;

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
     * （1）log日志配置文件也需要取出来 [%X{X-B3-TraceId}]
     * （2）http请求发送traceId放入header中
     */
    public static final String TRACE_ID = "traceId";

    public static final String SPAN_ID = "spanId";

    /**
     * 分布式traceId
     */
    private String traceId;
    /**
     * 分布式spanId
     */
    private String spanId;
}


