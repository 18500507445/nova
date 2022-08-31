package com.nova.tools.java8.grow.jdk8;

/**
 * 默认方法
 *
 * @author biezhi
 * @date 2018/2/8
 */
@FunctionalInterface
public interface FunctionalDefaultMethods {

    void method();

    default void defaultMethod() {
    }
}