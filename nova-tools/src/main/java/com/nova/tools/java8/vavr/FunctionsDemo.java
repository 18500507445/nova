package com.nova.tools.java8.vavr;


import io.vavr.Function2;

/**
 * @Description: 这里的Functions类似Java8里的Function，可以说是对Java8Function做的拓展，
 * 其中最大的拓展是Java8中的Function只支持定义一个参数和一个结果，
 * 但实际运用当中的话我们得有多个参数，那这样Java8的Function中必须得将参数包装成一个Bean，
 * 在Bean中去封装多个参数值，而Vavr则直接将其拓展，使Function可以支持多个参数，最多支持8个参数，创建示例如下：
 * @Author: wangzehui
 * @Date: 2022/10/13 16:15
 */
public class FunctionsDemo {

    public static void main(String[] args) {
        // 创建方式一
        Function2<Integer, Integer, Integer> sumOne = (a, b) -> a + b;

        // 创建方式二
        Function2<Integer, Integer, Integer> sumTwo = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return a + b;
            }
        };

        // 创建方式三
        Function2<Integer, Integer, Integer> sumThree = Function2.of((a, b) -> a + b);


        //创建方式四
        Function2<Integer, Integer, Integer> sumFour = Integer::sum;
        System.out.println(sumFour.apply(100, 100));
    }


}
