package com.nova.tools.vc.global;

/**
 * @author wangzehui
 * @date 2018/9/19 19:46
 * csdn：https://blog.csdn.net/xiaohuh421/article/details/44056189
 */

public class ForTwo {

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            System.out.println("外层循环" + i);
            for (int j = 0; j < 100; j++) {
                System.out.println("内层循环" + j);
            }
        }
        Long end = System.currentTimeMillis();
        System.out.println("调用时间" + (end - start) / 1000 + "秒");


    }


}