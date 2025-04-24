package com.nova.common.utils.copybean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author:wzh
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConvertToolUtils {

    /**
     * 利用反射实现对象之间相同属性复制
     *
     * @param source 要复制的
     * @param target 复制给
     */
    public static void copyProperties(Object source, Object target) throws Exception {
        copyPropertiesExclude(source, target, null);
    }

    /**
     * 复制对象属性
     *
     * @param from
     * @param to
     * @param excludeArray 排除属性列表
     * @throws Exception
     */
    public static void copyPropertiesExclude(Object from, Object to, String[] excludeArray) throws Exception {
        List<String> excludesList = null;
        if (excludeArray != null && excludeArray.length > 0) {
            //构造列表对象
            excludesList = Arrays.asList(excludeArray);
        }
        Method[] fromMethods = from.getClass().getDeclaredMethods();
        Method[] toMethods = to.getClass().getDeclaredMethods();
        Method fromMethod, toMethod;
        String fromMethodName, toMethodName;

        for (Method method : fromMethods) {
            fromMethod = method;
            fromMethodName = fromMethod.getName();
            if (!fromMethodName.contains("get")) {
                continue;
            }
            // 排除列表检测
            if (excludesList != null
                    && excludesList.contains(fromMethodName.substring(3)
                    .toLowerCase())) {
                continue;
            }
            toMethodName = "set" + fromMethodName.substring(3);
            toMethod = findMethodByName(toMethods, toMethodName);
            if (toMethod == null) {
                continue;
            }
            Object value = fromMethod.invoke(from);

            if (value == null) {
                continue;
            }
            // 集合类判空处理
            if (value instanceof Collection) {
                Collection<?> newValue = (Collection<?>) value;
                if (newValue.size() <= 0) {
                    continue;
                }
            }
            toMethod.invoke(to, value);
        }
    }

    /**
     * 对象属性值复制，仅复制指定名称的属性值
     *
     * @param from
     * @param to
     * @param includeArray
     * @throws Exception
     */
    public static void copyPropertiesInclude(Object from, Object to, String[] includeArray) throws Exception {
        List<String> includesList;
        if (includeArray != null && includeArray.length > 0) {
            includesList = Arrays.asList(includeArray);
        } else {
            return;
        }
        Method[] fromMethods = from.getClass().getDeclaredMethods();
        Method[] toMethods = to.getClass().getDeclaredMethods();
        Method fromMethod = null, toMethod = null;
        String fromMethodName = null, toMethodName = null;
        for (Method method : fromMethods) {
            fromMethod = method;
            fromMethodName = fromMethod.getName();
            if (!fromMethodName.contains("get")) {
                continue;
            }
            // 排除列表检测
            String str = fromMethodName.substring(3);
            if (!includesList.contains(str.substring(0, 1).toLowerCase()
                    + str.substring(1))) {
                continue;
            }
            toMethodName = "set" + fromMethodName.substring(3);
            toMethod = findMethodByName(toMethods, toMethodName);

            if (toMethod == null) {
                continue;
            }
            Object value = fromMethod.invoke(from);
            if (value == null) {
                continue;
            }
            // 集合类判空处理
            if (value instanceof Collection) {

                Collection<?> newValue = (Collection<?>) value;

                if (newValue.size() <= 0) {
                    continue;
                }
            }
            toMethod.invoke(to, value);
        }
    }

    /**
     * 从方法数组中获取指定名称的方法
     *
     * @param methods
     * @param name
     * @return
     */
    public static Method findMethodByName(Method[] methods, String name) {
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }
}
