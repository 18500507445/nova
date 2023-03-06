package com.nova.tools.leetcode.base;

import org.junit.jupiter.api.Test;

/**
 * @description: 数字翻转，非int类型返回0
 * @author: wzh
 * @date: 2021/2/23 17:13
 */

public class Reverse {

    public static void main(String[] args) {
        int x = 1001;
        System.out.println(calculation(x));
    }

    @Test
    public void demoC() {
        boolean palindrome = isPalindrome(5665);
        System.out.println("palindrome = " + palindrome);
    }

    public boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        int a = x;
        int sum = 0;
        while (x != 0) {
            sum = sum * 10 + x % 10;
            x /= 10;
        }
        return sum == a;
    }

    public static int calculation(int x) {
        int result;
        try {
            StringBuilder sb = new StringBuilder();
            int i = Integer.parseInt(sb.append(Math.abs(x)).reverse().toString());
            if (x > 0) {
                result = i;
            } else {
                result = -i;
            }
        } catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }
}
