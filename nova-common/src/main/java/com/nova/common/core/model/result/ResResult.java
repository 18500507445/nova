package com.nova.common.core.model.result;

import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.nova.common.trace.TraceContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @description: 通用返回结果对象
 * @author: wzh
 * @date: 2023/03/20 11:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResResult<T> extends HashMap<String, Object> implements Serializable {

    /**
     * 公网ip 最后2位，方便查找log
     */
    @Setter
    private static String internetIp = "00";

    //状态码，比如000000代表响应成功
    public static final String BIZ_CODE = "bizCode";

    //响应信息，用来说明响应情况
    public static final String BIZ_MESSAGE = "bizMessage";

    //data
    public static final String DATA = "data";

    //成功
    public static final String SUCCESS = "success";

    //分布式链路id
    public static final String TRACE_ID = "traceId";

    //分布式分片id
    public static final String SPAN_ID = "spanId";

    //系统时间
    public static final String SYSTEM_TIME = "systemTime";

    //当前环境
    public static final String ENV = "env";

    //ip
    public static final String IP = "ip";

    public ResResult() {

    }

    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public ResResult<T> put(@NotNull String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 手动存入Data
     *
     * @param value 值
     */
    public ResResult<T> setData(Object value) {
        super.put(DATA, value);
        return this;
    }

    /**
     * 序列化 ==> toJson
     */
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }

    public ResResult(IResultCode resultCode, T data, Boolean success) {
        super.put(BIZ_CODE, resultCode.getBizCode());
        super.put(BIZ_MESSAGE, resultCode.getBizMessage());
        super.put(DATA, data);
        super.put(SUCCESS, success);
        super.put(TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(SPAN_ID, TraceContext.getCurrentTrace().getSpanId());
        super.put(SYSTEM_TIME, System.currentTimeMillis());
        super.put(ENV, SpringUtil.getActiveProfile());
        super.put(IP, internetIp);
    }

    public ResResult(IResultCode resultCode, T data, Boolean success, String bizMessage) {
        super.put(BIZ_CODE, resultCode.getBizCode());
        super.put(BIZ_MESSAGE, bizMessage);
        super.put(DATA, data);
        super.put(SUCCESS, success);
        super.put(TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(SPAN_ID, TraceContext.getCurrentTrace().getSpanId());
        super.put(SYSTEM_TIME, System.currentTimeMillis());
        super.put(ENV, SpringUtil.getActiveProfile());
        super.put(IP, internetIp);
    }

    public ResResult(IResultCode resultCode, Boolean success) {
        super.put(BIZ_CODE, resultCode.getBizCode());
        super.put(BIZ_MESSAGE, resultCode.getBizMessage());
        super.put(SUCCESS, success);
        super.put(TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(SPAN_ID, TraceContext.getCurrentTrace().getSpanId());
        super.put(SYSTEM_TIME, System.currentTimeMillis());
        super.put(ENV, SpringUtil.getActiveProfile());
        super.put(IP, internetIp);
    }

    public ResResult(ResResult<T> resResult) {
        if (null != resResult) {
            super.put(BIZ_CODE, MapUtil.getStr(resResult, BIZ_CODE, ""));
            super.put(BIZ_MESSAGE, MapUtil.getStr(resResult, BIZ_MESSAGE, ""));
            super.put(DATA, resResult.get(DATA));
            super.put(SUCCESS, MapUtil.getBool(resResult, SUCCESS, false));
        }
        super.put(TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(SPAN_ID, TraceContext.getCurrentTrace().getSpanId());
        super.put(SYSTEM_TIME, System.currentTimeMillis());
        super.put(ENV, SpringUtil.getActiveProfile());
        super.put(IP, internetIp);
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
        return new ResResult<>(ResultCode.FAILED, false);
    }

    public static <T> ResResult<T> failure(T data) {
        return new ResResult<>(ResultCode.FAILED, data, false);
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

    public static <T> ResResult<T> build(ResResult<T> result) {
        return new ResResult<>(result);
    }
}
