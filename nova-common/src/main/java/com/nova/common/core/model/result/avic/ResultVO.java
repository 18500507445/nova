package com.nova.common.core.model.result.avic;

import cn.hutool.extra.spring.SpringUtil;
import com.nova.common.trace.TraceContext;
import lombok.Data;

import java.io.Serializable;

/**
 * ResultVO
 *
 * @author suo
 * @date 2023/8/23 21:15
 */
@Data
public class ResultVO<T> implements Serializable {

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

    /**
     * 系统时间
     */
    private Long systemTime;

    /**
     * 当前环境
     */
    private String env;

    public ResultVO() {

    }

    public ResultVO(IResultCode resultCode, T data, Boolean success) {
        this.bizCode = resultCode.getBizCode();
        this.bizMessage = resultCode.getBizMessage();
        this.data = data;
        this.success = success;
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.env = SpringUtil.getActiveProfile();
        this.systemTime = System.currentTimeMillis();
    }

    public ResultVO(IResultCode resultCode, T data, Boolean success, String bizMessage) {
        this.bizCode = resultCode.getBizCode();
        this.bizMessage = bizMessage;
        this.data = data;
        this.success = success;
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.env = SpringUtil.getActiveProfile();
        this.systemTime = System.currentTimeMillis();
    }

    public ResultVO(IResultCode resultCode) {
        this.bizCode = resultCode.getBizCode();
        this.bizMessage = resultCode.getBizMessage();
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.env = SpringUtil.getActiveProfile();
        this.systemTime = System.currentTimeMillis();
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO<T>(ResultCode.SUCCESS);
    }

    public static <T> ResultVO<T> success(IResultCode resultCode, String bizMessage) {
        return new ResultVO<T>(resultCode, null, true, bizMessage);
    }

    public static <T> ResultVO<T> success(IResultCode resultCode, T data) {
        return new ResultVO<T>(resultCode, data, true);
    }

    public static <T> ResultVO<T> success(IResultCode resultCode, T data, String bizMessage) {
        return new ResultVO<T>(resultCode, data, true, bizMessage);
    }

    public static <T> ResultVO<T> failure() {
        return new ResultVO<T>(ResultCode.FAILED);
    }

    public static <T> ResultVO<T> failure(IResultCode resultCode, String bizMessage) {
        return new ResultVO<T>(resultCode, null, false, bizMessage);
    }

    public static <T> ResultVO<T> failure(IResultCode resultCode, T data) {
        return new ResultVO<T>(resultCode, data, false);
    }

    public static <T> ResultVO<T> failure(IResultCode resultCode, T data, String bizMessage) {
        return new ResultVO<T>(resultCode, data, false, bizMessage);
    }

}
