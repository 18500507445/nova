package com.nova.common.core.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.common.utils.common.ServletUtils;
import com.nova.common.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * web层通用数据处理
 */
@Slf4j
public class BaseController {

    /**
     * 获取request
     */
    public HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    /**
     * 获取请求连接参数
     *
     * @param key
     */
    public String getValue(String key) {
        String value = "";
        HttpServletRequest request = getRequest();
        if (ObjectUtil.isNotNull(request)) {
            value = request.getParameter(key);
            if (ObjectUtil.isEmpty(value)) {
                value = request.getHeader(key);
            }
        }
        return value;
    }


    /**
     * 获取用户请求IP地址
     */
    public String getIp() {
        String createIp = IpUtils.getIpAddr(getRequest());
        if (StrUtil.isNotBlank(createIp)) {
            String[] split = createIp.split(",");
            if (split.length > 0) {
                if (StrUtil.isNotBlank(split[0])) {
                    return split[0].trim();
                }
            }
        }
        return "";
    }

    /**
     * 获取机器IP地址
     */
    public String getHostIp() {
        String createIp = IpUtils.getIpAddr(getRequest());
        if (StrUtil.isNotBlank(createIp)) {
            String[] split = createIp.split(",");
            if (split.length > 1) {
                if (StrUtil.isNotBlank(split[1])) {
                    return split[1].trim();
                }
            }
        }
        return "";
    }

    /**
     * request请求参数转换Map通用处理方法
     */
    public static Map<String, Object> convertDataMap(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();
        Map<String, Object> returnMap = new HashMap<>();
        Iterator<?> entries = properties.entrySet().iterator();
        Map.Entry<?, ?> entry;
        String name;
        StringBuilder value;
        while (entries.hasNext()) {
            entry = (Map.Entry<?, ?>) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = new StringBuilder();
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                value = new StringBuilder();
                for (String s : values) {
                    value.append(s).append(",");
                }
                if (value.length() > 0) {
                    value = new StringBuilder(value.substring(0, value.length() - 1));
                }
            } else {
                value = new StringBuilder(valueObj.toString());
            }
            returnMap.put(name, value.toString());
        }
        return returnMap;
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse() {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? success() : error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    /**
     * 返回成功数据
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }

    /**
     * 返回错误码消息
     */
    public AjaxResult error(AjaxResult.Type type, String message) {
        return new AjaxResult(type, message);
    }


}
