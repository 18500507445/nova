package com.nova.tools.demo.datatype;

/**
 * @author wangzehui
 * @date 2018/8/13 10:44
 */

public class DoubleExercise {

    public static void main(String[] args) {
        double m = 1.01;
        double n = 365 * 1;
        /**
         * m的n次幂
         */
        Double pow = Math.pow(m, n);

        /**
         * Double转long类型
         */
        long l = pow.longValue();

        System.out.println(l);


    }
}
