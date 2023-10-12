package com.nova.common.core.model.result;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nova.common.trace.TraceContext;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 通用返回结果对象
 * @author: wzh
 * @date: 2023/03/20 11:16
 */
@Data
public class RespResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结果码
     */
    private Integer code = RespResultCode.OK.getCode();

    /**
     * 结果信息
     */
    @Getter
    private String message = RespResultCode.OK.getMessage();

    /**
     * 详细的错误消息(开发看)
     */
    private String detailMessage = RespResultCode.OK.getDetailMessage();

    /**
     * 返回结果的数据对象
     */
    @Getter
    private T data;

    /**
     * 分布式链路traceId
     */
    private String traceId;

    /**
     * 分布式链路spanId
     */
    private String spanId;

    /**
     * 服务部署的环境
     */
    private String env;

    public RespResult() {
    }

    public RespResult(Integer code) {
        this.code = code;
    }

    public RespResult(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.spanId = TraceContext.getCurrentTrace().getSpanId();
        this.env = SpringUtil.getActiveProfile();
    }

    public RespResult(Integer code, String message, String detailMessage) {
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.spanId = TraceContext.getCurrentTrace().getSpanId();
        this.env = SpringUtil.getActiveProfile();
    }

    public RespResult(Integer code, String message, String detailMessage, String traceId, String spanId, String env) {
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
        this.traceId = traceId;
        this.spanId = spanId;
        this.env = env;
    }

    public RespResult(IRespResultCode respResultCode) {
        this.code = respResultCode.getCode();
        this.message = respResultCode.getMessage();
        this.detailMessage = respResultCode.getDetailMessage();
        this.traceId = TraceContext.getCurrentTrace().getTraceId();
        this.spanId = TraceContext.getCurrentTrace().getSpanId();
        this.env = SpringUtil.getActiveProfile();
    }

    public RespResult(IRespResultCode respResultCode, String traceId, String spanId, String env) {
        this.code = respResultCode.getCode();
        this.message = respResultCode.getMessage();
        this.detailMessage = respResultCode.getDetailMessage();
        this.traceId = traceId;
        this.spanId = spanId;
        this.env = env;
    }

    @JsonIgnore
    public boolean isOk() {
        return RespResultCode.OK.getCode().equals(code);
    }

    @JsonIgnore
    public boolean isNotOk() {
        return !this.isOk();
    }

    public static <T> RespResult<T> error(IRespResultCode apiResultCode) {
        return new RespResult<T>(apiResultCode);
    }

    public static <T> RespResult<T> error(IRespResultCode apiResultCode, T data) {
        return new RespResult<T>(apiResultCode).setData(data);
    }

    public static <T> RespResult<T> error(IRespResultCode apiResultCode, Object... params) {
        return new RespResult<T>(apiResultCode.getCode(), String.format(apiResultCode.getMessage(), params));
    }

    public static <T> RespResult<T> error(IRespResultCode apiResultCode, T data, Object... params) {
        return new RespResult<T>(apiResultCode.getCode(), String.format(apiResultCode.getMessage(), params))
                .setData(data);
    }

    public static <T> RespResult<T> error(String msg) {
        return new RespResult<T>(RespResultCode.SYS_EXCEPTION.getCode(), msg, msg);
    }

    public static <T> RespResult<T> error(String msg, String detailMessage) {
        return new RespResult<T>(RespResultCode.SYS_EXCEPTION.getCode(), msg, detailMessage);
    }

    public static <T> RespResult<T> error(String msg, T data) {
        return new RespResult<T>(RespResultCode.SYS_EXCEPTION.getCode(), msg, msg).setData(data);
    }

    public static <T> RespResult<T> error(String msg, String detailMessage, T data) {
        return new RespResult<T>(RespResultCode.SYS_EXCEPTION.getCode(), msg, detailMessage).setData(data);
    }

    public static <T> RespResult<T> error(Integer code, String msg) {
        return new RespResult<T>(code, msg, msg);
    }


    public static <T> RespResult<T> error(Integer code, String msg, String detailMessage) {
        return new RespResult<T>(code, msg, detailMessage);
    }

    public static <T> RespResult<T> error(Integer code, String msg, T data) {
        return new RespResult<T>(code, msg, msg).setData(data);
    }

    public static <T> RespResult<T> error(Integer code, String msg, String detailMessage, T data) {
        return new RespResult<T>(code, msg, detailMessage).setData(data);
    }

    public static <T> RespResult<T> error(List<Object> codeAndMsgList) {
        return new RespResult<>(Integer.valueOf(codeAndMsgList.get(0).toString()),
                codeAndMsgList.get(1).toString(), codeAndMsgList.get(1).toString());
    }

    public static <T> RespResult<T> error(List<Object> codeAndMsgList, T data) {
        RespResult<T> ar = error(codeAndMsgList);
        ar.setData(data);
        return ar;
    }

    public static RespResult<Void> success() {
        return new RespResult<>(RespResultCode.OK);
    }

    public static <T> RespResult<T> success(T data) {
        RespResult<T> ar = new RespResult<T>(RespResultCode.OK);
        ar.setData(data);
        return ar;
    }

    public static <T> RespResult<T> success(IRespResultCode apiResultCode) {
        return new RespResult<T>(apiResultCode);
    }

    public RespResult<T> setRespMessage(IRespResultCode respResultCode) {
        this.code = respResultCode.getCode();
        this.message = respResultCode.getMessage();
        this.detailMessage = respResultCode.getDetailMessage();
        return this;
    }

    public RespResult<T> setMessage(String msg) {
        this.message = msg;
        return this;
    }

    public RespResult<T> setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    public RespResult<T> setMessage(String msg, Object... params) {
        this.message = String.format(msg, params);
        return this;
    }

    public RespResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}
