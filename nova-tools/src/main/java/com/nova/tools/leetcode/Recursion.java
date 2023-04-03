package com.nova.tools.leetcode;

/**
 * @description: 求和1-100 递归
 * @author: wzh
 * @date: 2021/2/22 14:27
 */
class Recursion {

    public static void main(String[] args) {
        add(1);
        System.out.println(add(1));
    }

    static int add(int i) {
        if (i > 0 && i < 100) {
            return i + add(i + 1);
        } else {
            return i;
        }
    }


}
