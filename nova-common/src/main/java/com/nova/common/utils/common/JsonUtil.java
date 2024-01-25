package com.nova.common.utils.common;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

/**
 * @author: wzh
 * @description json工具类
 * @date: 2023/08/18 16:57
 */
@Slf4j(topic = "JsonUtil")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtil {

    public enum Type {
        INCLUDE,

        EXCLUDE
    }

    /**
     * json字段处理
     *
     * @param object    对象
     * @param fieldList 字段list
     * @param clazz     JSONObject.class或JSONArray.class
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
        String jsonString = JSON.toJSONString(object, filter);
        String name = clazz.getName();
        if (JSONObject.class.getName().equals(name)) {
            return (T) JSON.parseObject(jsonString);
        } else if (JSONArray.class.getName().equals(name)) {
            return (T) JSON.parseArray(jsonString);
        } else {
            return null;
        }
    }


    /**
     * 定义jackson对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     *
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("json parse err,data:{}", data, e);
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            log.error("json parse err,jsonData:{}", jsonData, e);
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     *
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            log.error("json parse err,jsonData:{}", jsonData, e);
        }
        return null;
    }

    /**
     * 将json结果集转化为Map
     *
     * @param jsonData json数据
     * @return
     */
    public static Map jsonToMap(String jsonData) {
        try {
            return JSONObject.parseObject(jsonData, Map.class);
        } catch (Exception e) {
            log.error("json parse err,jsonData:{}", jsonData, e);
        }
        return null;
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StrUtil.isEmpty(text)) {
            return new ArrayList<>();
        }
        try {
            return MAPPER.readValue(text, MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

    public static JsonNode parseTree(String text) {
        try {
            return MAPPER.readTree(text);
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

    public static JsonNode parseTree(byte[] text) {
        try {
            return MAPPER.readTree(text);
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

}


