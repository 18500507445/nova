package com.nova.tools.leetcode;

/**
 * @description: 打印99乘法表
 * @author: wzh
 * @date: 2021/3/3 11:05
 */

class MultiplicationTable {
    static void main(String[] args) {
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(j + "*" + i + "=" + i * j + "\t");
            }
            System.out.println();
        }
    }
}
