package com.nova.tools.demo.guava;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/10/13 18:26
 */
public class StringUtilsTest {

    /**
     * 12345678911
     * 脱敏
     * 123****8911
     */
    @Test
    public void test3() {
        String str = "12345678911";
        // 返回某个字符串左边的几个字符

        // 123
        String left = StringUtils.left(str, 3);
        // 8911
        String right = StringUtils.right(str, 4);
        System.out.println(left + "****" + right);
        // rightPad：如果left长度<7,那么就在右边用*填充到7个长度
        String padResult = StringUtils.rightPad(left, 7, '*');
        System.out.println(padResult + right);
    }

    /**
     * 判断null/""
     */
    @Test
    public void test2() {
        String str = "   ";
        System.out.println(StringUtils.isEmpty(str));
        // 反过来
        System.out.println(StringUtils.isNotEmpty(str));
    }

    /**
     * 判断null/""/"   "
     */
    @Test
    public void test1() {
        String str = "   ";
        System.out.println(StringUtils.isBlank(str));
        // 反过来
        System.out.println(StringUtils.isNotBlank(str));
    }
}
