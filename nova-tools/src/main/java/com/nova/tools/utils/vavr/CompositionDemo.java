package com.nova.tools.utils.vavr;

import io.vavr.Function1;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/13 16:19
 */
class CompositionDemo {

    public static void main(String[] args) {
        // (2 + 1) * 3 = 6
        Function1<Integer, Integer> func1 = a -> a + 1;
        Function1<Integer, Integer> func2 = a -> a * 2;
        Function1<Integer, Integer> func3 = func1.andThen(func2);
        System.out.println(func3.apply(2));

        Function1<Integer, Integer> func4 = func2.compose(func1);
        System.out.println(func4.apply(2));

    }
}
