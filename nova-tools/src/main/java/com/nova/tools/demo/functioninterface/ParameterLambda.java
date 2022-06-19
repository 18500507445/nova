package com.nova.tools.demo.functioninterface;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2021/4/20 21:14
 */

public class ParameterLambda {

    public static void main(String[] args) {
        ILove love;

        //1.lambda标识简化
        love = (int a) -> {
            System.out.println("I Love You-->" + a);
        };

        //2.简化1 参数类型
        love = (a) -> {
            System.out.println("I Love You-->" + a);
        };

        //3.简化2 简化括号
        love = a -> {
            System.out.println("I Love You-->" + a);
        };

        //4.简化3 去掉花括号
        love = a -> System.out.println("I Love You-->" + a);

        love.love(521);

        /**
         * 5.总结: lambda表达式只能有一行代码的情况下才能简化成一行，如果有多行,那么就用代码块包裹
         *   前提接口为函数式接口
         *   多个参数也可以去掉参数类型，要去掉就都去掉，必须加上括号
         */
    }
}

interface ILove {
    void love(int a);
}


