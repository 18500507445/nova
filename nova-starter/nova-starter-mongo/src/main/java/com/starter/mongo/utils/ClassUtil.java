package com.starter.mongo.utils;

import org.springframework.data.annotation.Id;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字节码工具
 *
 * @author wzh
 * @date 2023/6/13
 */
public class ClassUtil {

    private ClassUtil() {
    }

    /**
     * 缓存目标类上的泛型值
     */
    private static final Map<Class<?>, Class<?>> CACHE = new ConcurrentHashMap<>(64);

    /**
     * 缓存mongo实体的主键字段
     */
    private static final Map<Class<?>, Field> FIELD_CACHE = new ConcurrentHashMap<>(64);

    /**
     * 获取mongo集合实体的主键值
     *
     * @param obj 目标对象
     * @return id对应的值
     */
    public static Serializable getId(Object obj) {

        Field field = getIdField(obj);
        field.setAccessible(true);
        try {
            return (Serializable) field.get(obj);
        } catch (IllegalAccessException e) {
            throw ExceptionUtils.mpe("not exist id value");
        }

    }

    /**
     * 获取mongo集合实体的主键字段
     *
     * @param obj 目标对象
     * @return 主建字段
     */
    public static Field getIdField(Object obj) {

        Class<?> clazz = obj.getClass();
        Field result = FIELD_CACHE.get(clazz);
        if (Objects.nonNull(result)) {
            return result;
        }
        for (Field field : clazz.getDeclaredFields()) {
            Id annotation = field.getAnnotation(Id.class);
            if (Objects.nonNull(annotation)) {
                FIELD_CACHE.put(clazz, field);
                return field;
            }
        }
        throw ExceptionUtils.mpe("no exist id");

    }

    /**
     * 获取对象上的泛型
     *
     * @param obj 目标类
     * @return 类名
     */
    public static Class<?> getTClass(Object obj) {

        if (Objects.isNull(obj)) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        Class<?> result = CACHE.get(clazz);
        if (Objects.nonNull(result)) {
            return result;
        }
        Type aClass = obj.getClass().getGenericSuperclass();
        Type subType = ((ParameterizedTypeImpl) aClass).getActualTypeArguments()[0];
        result = (Class<?>) subType;
        CACHE.put(clazz, result);
        return result;

    }

}
