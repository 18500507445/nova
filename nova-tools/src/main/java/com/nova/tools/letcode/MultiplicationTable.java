package com.nova.tools.letcode;

/**
 * @Description: 打印99乘法表
 * @Author: wangzehui
 * @Date: 2021/3/3 11:05
 */

public class MultiplicationTable {
    public static void main(String[] args) {
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(j + "*" + i + "=" + i * j + "\t");
            }
            System.out.println();
        }
    }
}
