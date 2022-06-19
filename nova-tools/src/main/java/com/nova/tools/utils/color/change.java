package com.nova.tools.utils.color;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2021/3/31 10:20
 */

public class change {

    public static void main(String[] args) {
        String color = "C4D8F6";
        int r = Integer.parseInt((color.substring(0, 2)), 16);   //转为16进制
        int g = Integer.parseInt((color.substring(2, 4)), 16);
        int b = Integer.parseInt((color.substring(4, 6)), 16);

        System.out.println(r);
        System.out.println(g);
        System.out.println(b);
    }
}
