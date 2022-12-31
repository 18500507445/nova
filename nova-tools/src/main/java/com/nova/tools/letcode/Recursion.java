package com.nova.tools.letcode;

/**
 * @description: 求和1-100 递归
 * @author: wzh
 * @date: 2021/2/22 14:27
 */
public class Recursion {
    public static void main(String[] args) {
        add(1);
        System.out.println(add(1));
    }

    public static int add(int i) {
        if (i > 0 && i < 100) {
            return i + add(i + 1);
        } else {
            return i;
        }
    }


}
