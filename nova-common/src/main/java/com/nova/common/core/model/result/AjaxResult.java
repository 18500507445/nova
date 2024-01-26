package com.nova.common.core.model.result;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * 返回实体类
 *
 * @author wangzehui
 */
@Setter
@Getter
public class AjaxResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "code";

    public static final String MSG_TAG = "msg";

    public static final String DATA_TAG = "data";

    public static final String FUNCTION_TAG = "function";

    /**
     * 状态类型
     */
    public enum Type {
        /**
         * 成功
         */
        SUCCESS("0"),
        /**
         * 警告
         */
        WARN("301"),
        /**
         * 错误
         */
        ERROR("500");
        private final String value;


        Type(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    /**
     * 状态类型
     */
    private Type type;

    /**
     * 状态码
     */
    private String code;

    /**
     * 返回内容
     */
    private String msg;

    /**
     * 数据对象
     */
    private Object data;

    /**
     * 获取请求过来的参数，取出api中的function名称，添加到返回值中
     */
    protected String getApiRequestFunction() {
        String function = "";
        //try {
        //    HttpServletRequest request = ServletUtils.getRequest();
        //    String apiUrl = request.getParameter("accessSecretData");
        //    if (ObjectUtil.isEmpty(apiUrl)) {
        //        return request.getRequestURI();
        //    }
        //    apiUrl = SecurityUtil.DecryptAllPara(apiUrl, "");
        //    String[] arrUrl = apiUrl.split("&");
        //    Map<String, String> urlParam = new HashMap<>();
        //    for (String str : arrUrl) {
        //        String[] arrs = str.split("=");
        //        if (arrs.length == 1) {
        //            urlParam.put(arrs[0], "");
        //        } else {
        //            urlParam.put(arrs[0], arrs[1]);
        //        }
        //    }
        //    for (Entry<String, String> entry : urlParam.entrySet()) {
        //        if ("function".equals(entry.getKey())) {
        //            function = URLDecoder.decode(entry.getValue(), "utf-8");
        //            break;
        //        }
        //    }
        //} catch (UnsupportedEncodingException e) {
        //    e.printStackTrace();
        //}
        return function;
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     */
    public AjaxResult(Type type, String msg) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        super.put(FUNCTION_TAG, getApiRequestFunction());
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AjaxResult(String msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        super.put(FUNCTION_TAG, getApiRequestFunction());
        if (ObjectUtil.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AjaxResult(Type type, String msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        super.put(FUNCTION_TAG, getApiRequestFunction());
        if (ObjectUtil.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态编码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AjaxResult(String code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        super.put(FUNCTION_TAG, getApiRequestFunction());
        if (ObjectUtil.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success("请求成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success("请求成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(Type.SUCCESS, msg, data);
    }

    /**
     * 返回成功消息
     *
     * @param code 返回码
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String code, String msg, Object data) {
        return new AjaxResult(code, msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param data 返回内容
     * @return 警告消息
     */
    public static AjaxResult warn(Object data) {
        return AjaxResult.warn("警告", data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult warn(String msg) {
        return AjaxResult.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult warn(String msg, Object data) {
        return new AjaxResult(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static AjaxResult error() {
        return AjaxResult.error("请求失败");
    }

    /**
     * 返回错误消息
     *
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(Object data) {
        return AjaxResult.error("请求失败", data);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(Type.ERROR.value, msg);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(Type.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 返回编码
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String code, String msg, Object data) {
        return new AjaxResult(code, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code
     * @param msg
     * @return
     */
    public static AjaxResult error(String code, String msg) {
        return new AjaxResult(code, msg, null);
    }


}
