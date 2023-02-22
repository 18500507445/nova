package com.nova.ebook.effectivejava.chapter4.section5;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: ?就叫做通配符
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest5 {

    public static double error(List<Number> numbers) {
        double sum = 0;
        for (Number number : numbers) {
            sum += number.doubleValue();
        }
        return sum;
    }

    /**
     * Number的子类都可以进行sum
     *
     * @param numbers
     * @return
     */
    public static double sum(List<? extends Number> numbers) {
        double sum = 0;
        for (Number number : numbers) {
            sum += number.doubleValue();
        }
        return sum;
    }

    /**
     * super和extends作用相反
     * <p>
     * 相当于Number继承了?，add(List<Object> list,Number n)，所以list放n就没有问题了
     *
     * @param list
     * @param n
     */
    public static void add(List<? super Number> list, Number n) {
        list.add(n);
    }

    /**
     * 可以简写成List<?>
     *
     * @param list
     */
    public static void test(List<? extends Object> list) {
        list.get(0);
    }

    /**
     * 不能匹配
     */
    @Test
    public void demoA() {
        //SectionTest5.error(new ArrayList<Double>());
    }

    @Test
    public void demoB() {
        List<Integer> intList = new ArrayList<>();

        List<Double> doubleList = new ArrayList<>();

        SectionTest5.sum(intList);
        SectionTest5.sum(doubleList);

        Number a = 1;
        Number b = 1.9d;
    }


}
