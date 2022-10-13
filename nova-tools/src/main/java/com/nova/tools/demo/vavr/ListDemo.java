package com.nova.tools.demo.vavr;

import io.vavr.collection.List;
import io.vavr.control.Option;

/**
 * @Description: list计算
 * @Author: wangzehui
 * @Date: 2022/10/13 16:22
 */
public class ListDemo {

    public static void main(String[] args) {

        List<Double> of = List.of(1.1, 2.2, 3.3);

        Number sum = of.sum();

        Option<Double> average = of.average();

        Option<Double> max = of.max();

        Option<Double> min = of.min();

        System.out.println(sum.doubleValue());

        System.out.println(average.get().doubleValue());

        System.out.println(max.get().doubleValue());

        System.out.println(min.get().doubleValue());

    }
}
