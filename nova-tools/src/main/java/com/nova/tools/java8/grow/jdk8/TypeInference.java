package com.nova.tools.java8.grow.jdk8;

/**
 * 更好的类型推断
 *
 * @author wzh
 * @date 2018/2/8
 */
public class TypeInference<T> {

    public static <T> T defaultValue() {
        return null;
    }

    public T getOrDefault(T value, T defaultValue) {
        return (value != null) ? value : defaultValue;
    }

    public static void main(String[] args) {
        final TypeInference<String> typeInference = new TypeInference<>();
        typeInference.getOrDefault("22", TypeInference.defaultValue());
    }

}