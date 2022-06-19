package com.nova.tools.demo.string;


import com.nova.tools.demo.entity.Myself;

/**
 * @author wangzehui
 * @date 2018/9/10 17:34
 */

public class StringExercise {

    private static String seatCode = Myself.SEAT_CODE;

    public static void main(String[] args) {
//        replaceStr();
        splitStr();

    }

    /**
     * 有特殊字符，加\\
     */
    private static void splitStr() {
        String[] split = seatCode.split("_");
        for (String s : split) {
            System.out.println(s);
        }
    }


    /**
     * replace和replaceAll的区别
     * replace是单纯的替换字符串，而replaceAll是替换匹配的正则表达式
     */
    private static void replaceStr() {
        seatCode = seatCode.replaceAll("_", "#");
        System.out.println(seatCode);
    }
}
