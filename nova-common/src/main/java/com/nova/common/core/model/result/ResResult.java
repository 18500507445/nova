package com.nova.common.core.model.result;

import cn.hutool.extra.spring.SpringUtil;
import com.nova.common.trace.TraceContext;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 通用返回结果对象
 * @author: wzh
 * @date: 2023/03/20 11:16
 */
@Data
public class ResResult<T> implements Serializable {

    /**
     * 状态码，比如000000代表响应成功
     */
    private String bizCode;
    /**
     * 响应信息，用来说明响应情况
     */
    private String bizMessage;
    /**
     * 响应的具体数据
     */
    private T data;

    private Boolean success;

    /**
     * 分布式链路id
     */
    private String traceId;

    private String spanId;

    /**
     * 系统时间
     */
    private Long systemTime;

    /**
     * 当前环境
     */
    private String env;

    public ResResult() {

    }

    public ResResult(IResultCode resultCode, T data, Boolean success) {
        this.bizCode = resultCode.getBizCode();
        this.bizMessage = resultCode.getBizMessage();
        this.data = data;
        this.success = success;
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.spanId = TraceContext.getCurrentTrace().getSpanId();
        this.env = SpringUtil.getActiveProfile();
        this.systemTime = System.currentTimeMillis();
    }

    public ResResult(IResultCode resultCode, T data, Boolean success, String bizMessage) {
        this.bizCode = resultCode.getBizCode();
        this.bizMessage = bizMessage;
        this.data = data;
        this.success = success;
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.spanId = TraceContext.getCurrentTrace().getSpanId();
        this.env = SpringUtil.getActiveProfile();
        this.systemTime = System.currentTimeMillis();
    }

    public ResResult(IResultCode resultCode, Boolean success) {
        this.bizCode = resultCode.getBizCode();
        this.bizMessage = resultCode.getBizMessage();
        this.success = success;
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.spanId = TraceContext.getCurrentTrace().getSpanId();
        this.env = SpringUtil.getActiveProfile();
        this.systemTime = System.currentTimeMillis();
    }

    public static <T> ResResult<T> success() {
        return new ResResult<>(ResultCode.SUCCESS, true);
    }

    public static <T> ResResult<T> success(T data) {
        return new ResResult<>(ResultCode.SUCCESS, data, true);
    }

    public static <T> ResResult<T> success(IResultCode resultCode) {
        return new ResResult<>(resultCode, true);
    }

    public static <T> ResResult<T> success(IResultCode resultCode, T data) {
        return new ResResult<>(resultCode, data, true);
    }

    public static <T> ResResult<T> success(IResultCode resultCode, String bizMessage) {
        return new ResResult<>(resultCode, null, true, bizMessage);
    }

    public static <T> ResResult<T> success(IResultCode resultCode, T data, String bizMessage) {
        return new ResResult<>(resultCode, data, true, bizMessage);
    }

    public static <T> ResResult<T> failure() {
        return new ResResult<>(ResultCode.FAILED, true);
    }

    public static <T> ResResult<T> failure(String bizMessage) {
        return new ResResult<>(ResultCode.FAILED, null, false, bizMessage);
    }

    public static <T> ResResult<T> failure(IResultCode resultCode) {
        return new ResResult<>(resultCode, false);
    }

    public static <T> ResResult<T> failure(IResultCode resultCode, String bizMessage) {
        return new ResResult<>(resultCode, null, false, bizMessage);
    }

    public static <T> ResResult<T> failure(IResultCode resultCode, T data) {
        return new ResResult<>(resultCode, data, false);
    }

    public static <T> ResResult<T> failure(IResultCode resultCode, T data, String bizMessage) {
        return new ResResult<>(resultCode, data, false, bizMessage);
    }
}
