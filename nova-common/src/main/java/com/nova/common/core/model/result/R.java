package com.nova.common.core.model.result;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.nova.common.core.model.result.code.IResultCode;
import com.nova.common.core.model.result.code.ResultCode;
import com.nova.common.trace.TraceContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @description: 通用返回结果对象
 * @author: wzh
 * @date: 2023/03/20 11:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class R<T> extends HashMap<String, Object> implements Serializable {

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

    //系统时间
    public static final String SYSTEM_TIME = "systemTime";

    //当前环境
    public static final String ENV = "env";

    //ip
    public static final String IP = "ip";

    public R() {

    }

    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public R<T> put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }

    public R(IResultCode resultCode, T data, Boolean success) {
        super.put(BIZ_CODE, resultCode.getBizCode());
        super.put(BIZ_MESSAGE, resultCode.getBizMessage());
        super.put(DATA, data);
        super.put(SUCCESS, success);
        super.put(TRACE_ID, TraceContext.getCurrentTrace().getTraceId());
        super.put(SYSTEM_TIME, System.currentTimeMillis());
        super.put(ENV, SpringUtil.getActiveProfile());
        super.put(IP, internetIp);
    }

    public static <T> R<T> success(T data) {
        return new R<>(ResultCode.SUCCESS, data, true);
    }

}
