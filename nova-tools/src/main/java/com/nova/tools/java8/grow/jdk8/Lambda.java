package com.nova.tools.java8.grow.jdk8;

import java.util.Arrays;

/**
 * Lambda 表达式
 *
 * @author wzh
 * @date 2018/2/8
 */
class Lambda {

    public static void main(String[] args) {
        Arrays.asList("a", "b", "d").forEach(System.out::println);
    }
}
