package com.nova.common.core.model.result;

import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
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
public class ResResult<T> extends HashMap<String, Object> implements Serializable {

    /**
     * 内部常量类
     */
    private static class Constants {
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
    }

    /**
     * 公网ip 最后2位，方便查找log
     */
    @Setter
    private static String internetIp = "00";

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
        super.put(Constants.DATA, value);
        return this;
    }

    /**
     * 序列化 ==> toJson
     */
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    // --------------------------------- 构造开始 ---------------------------------
    public ResResult() {

    }

    public ResResult(IResultCode resultCode, T data, Boolean success) {
        super.put(Constants.BIZ_CODE, resultCode.getBizCode());
        super.put(Constants.BIZ_MESSAGE, resultCode.getBizMessage());
        super.put(Constants.DATA, data);
        super.put(Constants.SUCCESS, success);
        super.put(Constants.TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(Constants.SPAN_ID, TraceContext.getCurrentTrace().getSpanId());
        super.put(Constants.SYSTEM_TIME, System.currentTimeMillis());
        super.put(Constants.ENV, SpringUtil.getActiveProfile());
        super.put(Constants.IP, internetIp);
    }

    public ResResult(IResultCode resultCode, T data, Boolean success, String bizMessage) {
        super.put(Constants.BIZ_CODE, resultCode.getBizCode());
        super.put(Constants.BIZ_MESSAGE, bizMessage);
        super.put(Constants.DATA, data);
        super.put(Constants.SUCCESS, success);
        super.put(Constants.TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(Constants.SPAN_ID, TraceContext.getCurrentTrace().getSpanId());
        super.put(Constants.SYSTEM_TIME, System.currentTimeMillis());
        super.put(Constants.ENV, SpringUtil.getActiveProfile());
        super.put(Constants.IP, internetIp);
    }

    public ResResult(IResultCode resultCode, Boolean success) {
        super.put(Constants.BIZ_CODE, resultCode.getBizCode());
        super.put(Constants.BIZ_MESSAGE, resultCode.getBizMessage());
        super.put(Constants.SUCCESS, success);
        super.put(Constants.TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(Constants.SPAN_ID, TraceContext.getCurrentTrace().getSpanId());
        super.put(Constants.SYSTEM_TIME, System.currentTimeMillis());
        super.put(Constants.ENV, SpringUtil.getActiveProfile());
        super.put(Constants.IP, internetIp);
    }

    public ResResult(ResResult<T> resResult) {
        if (null != resResult) {
            super.put(Constants.BIZ_CODE, MapUtil.getStr(resResult, Constants.BIZ_CODE, ""));
            super.put(Constants.BIZ_MESSAGE, MapUtil.getStr(resResult, Constants.BIZ_MESSAGE, ""));
            super.put(Constants.DATA, resResult.get(Constants.DATA));
            super.put(Constants.SUCCESS, MapUtil.getBool(resResult, Constants.SUCCESS, false));
        }
        super.put(Constants.TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(Constants.SPAN_ID, TraceContext.getCurrentTrace().getSpanId());
        super.put(Constants.SYSTEM_TIME, System.currentTimeMillis());
        super.put(Constants.ENV, SpringUtil.getActiveProfile());
        super.put(Constants.IP, internetIp);
    }

    // --------------------------------- 构造结束 ---------------------------------


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

    //转换构造
    public static <T> ResResult<T> build(ResResult<T> result) {
        return new ResResult<>(result);
    }
}
