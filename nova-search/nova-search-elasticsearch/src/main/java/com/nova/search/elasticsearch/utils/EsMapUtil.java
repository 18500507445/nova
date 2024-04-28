package com.nova.search.elasticsearch.utils;

import com.nova.search.elasticsearch.annotation.IGetterFunction;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wzh
 * @description: 工具类
 */
public class EsMapUtil extends LinkedHashMap<String, Object> {

    public <T> EsMapUtil put(IGetterFunction<T> fn, Object value) {
        String key = getFieldName(fn);
        super.put(key, value);
        return this;
    }

    public <T> EsMapUtil putStr(String key, Object value) {
        super.put(key, value);
        return this;
    }

    private static final Map<Serializable, SerializedLambda> CLASS_LAMBDA_CACHE = new ConcurrentHashMap<>();

    /***
     * 转换方法引用为属性名
     * @param fn
     */
    public <T> String getFieldName(IGetterFunction<T> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        String prefix = "";
        if (methodName.startsWith("get")) {
            prefix = "get";
        }
        // 截取get之后的字符串并转换首字母为小写
        return toLowerCaseFirstOne(methodName.replace(prefix, ""));
    }

    /**
     * 首字母转小写
     *
     * @param s
     */
    public String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    public static SerializedLambda getSerializedLambda(Serializable fn) {
        SerializedLambda lambda = CLASS_LAMBDA_CACHE.get(fn.getClass());
        if (lambda == null) {
            try {
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMBDA_CACHE.put(fn.getClass(), lambda);
            } catch (Exception e) {
                System.out.println("getSerializedLambda异常 = " + e.getMessage());
            }
        }
        return lambda;
    }
}