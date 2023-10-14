package com.nova.common.utils.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * request请求参数转换Map通用处理方法
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapDataUtil {

    public static Map<String, Object> convertDataMap(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Iterator<?> entries = properties.entrySet().iterator();
        Entry<?, ?> entry;
        String name = "";
        StringBuilder value;
        while (entries.hasNext()) {
            entry = (Entry<?, ?>) entries.next();
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
}
