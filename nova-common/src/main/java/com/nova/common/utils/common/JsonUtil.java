package com.nova.common.utils.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson2.JSON;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: wzh
 * @description json工具类
 * @date: 2023/08/18 16:57
 */
public class JsonUtil {

    public enum Type {
        INCLUDE,

        EXCLUDE
    }

    private JsonUtil() {

    }

    /**
     * json字段处理
     *
     * @param object    对象
     * @param fieldList 字段list
     * @param clazz     object.class
     * @param type      枚举INCLUDE包含，EXCLUDE排除
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T fastJsonField(Object object, List<String> fieldList, Class<T> clazz, Type type) {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        Set<String> set = new HashSet<>();
        switch (type) {
            case INCLUDE:
                set = filter.getIncludes();
                break;
            case EXCLUDE:
                set = filter.getExcludes();
                break;
        }
        set.addAll(fieldList);
        String jsonString = JSONObject.toJSONString(object, filter);
        String name = clazz.getName();
        if (JSONObject.class.getName().equals(name) || com.alibaba.fastjson2.JSONObject.class.getName().equals(name)) {
            return (T) JSON.parseObject(jsonString);
        } else if (JSONArray.class.getName().equals(name) || com.alibaba.fastjson2.JSONArray.class.getName().equals(name)) {
            return (T) JSON.parseArray(jsonString);
        } else {
            return null;
        }
    }
}


