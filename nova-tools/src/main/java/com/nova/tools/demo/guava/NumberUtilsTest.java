package com.nova.tools.demo.guava;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/10/13 18:25
 */
public class NumberUtilsTest {

    /**
     * 给我判断一个参数是不是数字(整数、浮点数)
     */
    @Test
    public void test1() {
        String str = "12.3aaa";
        // isDigits只能判断整数,也就是参数只能包含数字的时候才返回true,都是当做10进制来处理的
        System.out.println(str + "isDigits结果:" + NumberUtils.isDigits(str));
        // isParsable可以判断是不是整数、浮点数,不能识别正负,都是当做10进制来处理的
        System.out.println(str + "isParsable结果:" + NumberUtils.isParsable(str));
        // isCreatable可以判断是不是整数、浮点数,识别正负号，以及进制
        System.out.println(str + "isCreatable结果:" + NumberUtils.isCreatable(str));
        str = "12.3";
        System.out.println(str + "isDigits结果:" + NumberUtils.isDigits(str));
        System.out.println(str + "isParsable结果:" + NumberUtils.isParsable(str));
        System.out.println(str + "isCreatable结果:" + NumberUtils.isCreatable(str));
        str = "+12.3";
        System.out.println(str + "isDigits结果:" + NumberUtils.isDigits(str));
        System.out.println(str + "isParsable结果:" + NumberUtils.isParsable(str));
        System.out.println(str + "isCreatable结果:" + NumberUtils.isCreatable(str));
        str = "12";
        System.out.println(str + "isDigits结果:" + NumberUtils.isDigits(str));
        System.out.println(str + "isParsable结果:" + NumberUtils.isParsable(str));
        System.out.println(str + "isCreatable结果:" + NumberUtils.isCreatable(str));
        str = "09";
        System.out.println(str + "isDigits结果:" + NumberUtils.isDigits(str));
        System.out.println(str + "isParsable结果:" + NumberUtils.isParsable(str));
        // 以0开头认为是8进制
        System.out.println(str + "isCreatable结果:" + NumberUtils.isCreatable(str));

    }
}
