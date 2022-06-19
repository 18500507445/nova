package com.nova.tools.demo.datatype;

/**
 * 运算符
 * @author wangzehui
 * @date 2018/10/17 14:55
 */

public class Operator {
    public static void main (String[] args){

        /**
         *  = 赋值
         *  ==判断(地址和数值)
         *  .equals（数值）
         */

        /**
         * 条件都为true，结果为true
         * 一假则假
         */
        boolean a = true;
        boolean b = true;
        boolean c = (a = (1 == 2)) && (b = (1 == 2));
        System.out.println("短路和判断"+c);


        /**
         * 条件之一为true，结果为true
         *
         * 一真则真
         */
        boolean e = false;
        boolean f = false;

        boolean g = (e=(1==1)) || (f=(1==1));
        System.out.println("短路或判断"+g);






    }


}
