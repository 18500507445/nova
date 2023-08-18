package com.nova.common.utils.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

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
     * 展示字段处理
     *
     * @param object    对象
     * @param fieldList 字段list
     * @param clazz     object.class
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
        if (JSONObject.class.getName().equals(name)) {
            return (T) JSONObject.parseObject(jsonString);
        } else if (JSONArray.class.getName().equals(name)) {
            return (T) JSONObject.parseArray(jsonString);
        } else {
            return null;
        }
    }
}


