package com.nova.tools.utils.check;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2020/4/26 9:38
 */

public class VerificationCode {
    public static void main(String[] args) {
        System.out.println((int) ((Math.random() * 899999) + 100000));
    }
}
