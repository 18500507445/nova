package com.nova.common.context;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description: 获取请求参数工具类
 * @author: wangzehui
 * @date: 2022/12/20 11:16
 */
@Slf4j
public class RequestParamsUtil {

    private RequestParamsUtil() {

    }

    public static Map<String, Object> getFormParams(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>(16);
        Map<String, String[]> map = request.getParameterMap();
        Set<String> key = map.keySet();
        for (String k : key) {
            paramMap.put(k, map.get(k)[0]);
        }
        return paramMap;
    }

    public static Map<String, Object> getUrlParams(HttpServletRequest request) {
        String param = "";
        Map<String, Object> result = new HashMap<>(16);
        String queryString = request.getQueryString();
        if (StrUtil.isEmpty(queryString)) {
            return result;
        }
        try {
            param = URLDecoder.decode(queryString, "utf-8");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        String[] params = param.split("&");
        for (String s : params) {
            int index = s.indexOf('=');
            result.put(s.substring(0, index), s.substring(index + 1));
        }
        return result;
    }
}