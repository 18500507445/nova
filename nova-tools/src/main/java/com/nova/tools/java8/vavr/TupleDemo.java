package com.nova.tools.java8.vavr;

import io.vavr.Tuple;
import io.vavr.Tuple2;

/**
 * @Description: 元组是将多个不同类型的数据包裹的一个容器，比如可以将字符串，数字，数组用一个元组包裹，这样即可以通过一个元组变量获取到包括的任一数据。
 * @Author: wangzehui
 * @Date: 2022/10/13 16:06
 */
public class TupleDemo {

    public static final Tuple2<String, Integer> JAVA8 = Tuple.of("Java", 8);

    public static void main(String[] args) {
        String key = JAVA8._1;
        Integer value = JAVA8._2;
        System.out.println("key:" + key + ",value:" + value);

        /**
         * 这里有两种方式
         * 方式一：map后多个处理函数
         * 方式二：map后一个处理函数
         */
        Tuple2<String, Integer> that1 = JAVA8.map(
                s -> s.substring(2) + "vr",
                i -> i / 8
        );

        Tuple2<String, Integer> that2 = JAVA8.map(
                (s, i) -> Tuple.of(s.substring(2) + "vr", i / 8)
        );

        String that = JAVA8.apply(
                (s, i) -> s.substring(2) + "vr " + i / 8);
        System.out.println(that);

    }
}
