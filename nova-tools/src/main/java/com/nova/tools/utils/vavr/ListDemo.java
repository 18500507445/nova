package com.nova.tools.utils.vavr;

import io.vavr.collection.List;
import io.vavr.control.Option;

/**
 * @description: list计算
 * @author: wzh
 * @date: 2022/10/13 16:22
 */
class ListDemo {

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
