package com.nova.ebook.effectivejava.chapter7.section7;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest7 {

    @Test
    public void demoA() {
        String s1 = "w";
        String s2 = "z";
        String s3 = "h";

        String ss = s1 + s2 + s3;

        //内部会优化成
        String s = new StringBuilder().append(s1).append(s2).append(s3).toString();
        System.out.println(s);

    }

    @Test
    public void demoB() {
        String sum = "";
        for (int i = 0; i < 100; i++) {
            sum += "," + i;
        }
        System.out.println("sum = " + sum);
    }

    @Test
    public void demoC() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append(",").append(i);
        }
        System.out.println("sb" + sb);
    }


}
