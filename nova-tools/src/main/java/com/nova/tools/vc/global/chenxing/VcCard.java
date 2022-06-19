package com.nova.tools.vc.global.chenxing;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/1/24 17:50
 */

public class VcCard {

    public static void main(String[] args) {

        String str = "VC卡";
        if (str.toUpperCase().contains("VC卡")) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        if(str.toUpperCase().indexOf("VC卡") != -1){
            System.out.println("1");
        }else {
            System.out.println("2");
        }
    }
}
