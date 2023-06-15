package com.nova.tools.utils.hutool.core.lang.reflect;

import cn.hutool.core.lang.reflect.ActualTypeMapperPool;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 见：https://gitee.com/dromara/hutool/pulls/447/files
 * <p>
 * TODO 同时继承泛型和实现泛型接口需要解析，此处为F
 */
public class ActualTypeMapperPoolTest {

    @Test
    public void getTypeArgumentTest() {
        final Map<Type, Type> typeTypeMap = ActualTypeMapperPool.get(FinalClass.class);
        typeTypeMap.forEach((key, value) -> {
            if ("A".equals(key.getTypeName())) {
                Assert.equals(Character.class, value);
            } else if ("B".equals(key.getTypeName())) {
                Assert.equals(Boolean.class, value);
            } else if ("C".equals(key.getTypeName())) {
                Assert.equals(String.class, value);
            } else if ("D".equals(key.getTypeName())) {
                Assert.equals(Double.class, value);
            } else if ("E".equals(key.getTypeName())) {
                Assert.equals(Integer.class, value);
            }
        });
    }

    @Test
    public void getTypeArgumentStrKeyTest() {
        final Map<String, Type> typeTypeMap = ActualTypeMapperPool.getStrKeyMap(FinalClass.class);
        typeTypeMap.forEach((key, value) -> {
            if ("A".equals(key)) {
                Assert.equals(Character.class, value);
            } else if ("B".equals(key)) {
                Assert.equals(Boolean.class, value);
            } else if ("C".equals(key)) {
                Assert.equals(String.class, value);
            } else if ("D".equals(key)) {
                Assert.equals(Double.class, value);
            } else if ("E".equals(key)) {
                Assert.equals(Integer.class, value);
            }
        });
    }

    public interface BaseInterface<A, B, C> {
    }

    public interface FirstInterface<A, B, D, E> extends BaseInterface<A, B, String> {
    }

    public interface SecondInterface<A, B, F> extends BaseInterface<A, B, String> {
    }

    public static class BaseClass<A, D> implements FirstInterface<A, Boolean, D, Integer> {
    }

    public static class FirstClass extends BaseClass<Character, Double> implements SecondInterface<Character, Boolean, FirstClass> {
    }

    public static class SecondClass extends FirstClass {
    }

    public static class FinalClass extends SecondClass {
    }
}
