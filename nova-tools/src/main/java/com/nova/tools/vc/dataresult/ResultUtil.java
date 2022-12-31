package com.nova.tools.vc.dataresult;

import com.alibaba.fastjson2.JSONObject;
import com.nova.tools.vc.enumerate.ResultCode;

import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2022/9/21 17:24
 */
public class ResultUtil {

    public static String makeSuccessResult() {
        return makeResult(ResultCode.SUCCESS);
    }

    public static String makeSuccessResult(Object data) {
        return makeResult(ResultCode.SUCCESS, data);
    }

    public static String makeResult(String code, String message) {
        return makeResult(code, message, null);
    }

    public static String makeResult(String code, String message, Object data) {
        JSONObject object = new JSONObject();
        object.put("code", code);
        object.put("message", message);
        if (null != data) {
            object.put("data", data);
        }
        return object.toString();
    }

    public static String makeResult(ResultCode resultCode, Object data) {
        if (null == resultCode) {
            resultCode = ResultCode.ERROR;
        }
        return makeResult(resultCode.getCode(), resultCode.getMessage(), data);
    }

    public static String makeResult(ResultCode resultCode) {
        if (null == resultCode) {
            resultCode = ResultCode.ERROR;
        }
        return makeResult(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 返回值
     *
     * @param resultCode 返回码
     * @param data       数据
     * @param extra      额外返回值
     * @return
     */
    public static String makeResult(ResultCode resultCode, Object data, Map<String, Object> extra) {
        if (null == resultCode) {
            resultCode = ResultCode.ERROR;
        }
        JSONObject object = new JSONObject();
        object.put("code", resultCode.getCode());
        object.put("message", resultCode.getMessage());
        object.put("data", data);
        object.putAll(extra);
        return object.toString();
    }

    /**
     * 自定义message
     *
     * @param resultCode
     * @param message
     * @return
     */
    public static String makeResultCus(ResultCode resultCode, String message) {
        if (null == resultCode) {
            resultCode = ResultCode.ERROR;
        }
        return makeResult(resultCode.getCode(), message, null);
    }

}
