package com.nova.shopping.common.constant;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wzh
 * @description
 * @date: 2023/10/13 17:47
 */
public class BaseController {

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = getRequestAttributes();
        if (ObjectUtil.isNotNull(requestAttributes)) {
            return getRequestAttributes().getRequest();
        }
        return null;
    }


    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("client-ip");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取机器IP地址
     */
    public String getHostIp() {
        String createIp = getIpAddr(getRequest());
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
     * 获取用户请求IP地址
     */
    public static String getIp() {
        String createIp = getIpAddr(getRequest());
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
     * 获取请求连接参数
     *
     * @param key
     * @return
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
}
