package com.nova.tools.letcode;

/**
 * @Description: 数字翻转，非int类型返回0
 * @Author: wangzehui
 * @Date: 2021/2/23 17:13
 */

public class Reverse {

    public static void main(String[] args) {
        int x = 9646;
        System.out.println(calculation(x));
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
