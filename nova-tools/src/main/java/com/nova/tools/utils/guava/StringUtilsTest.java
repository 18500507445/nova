package com.nova.tools.utils.guava;

import com.google.common.base.CharMatcher;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/13 18:26
 */
class StringUtilsTest {

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

    /**
     * 字符串的处理
     */
    @Test
    public void charMatcherTest() {
        String str = "aj\tld1\b23aAbCs  kF45JAb  c56sl";
        //移除str中的a
        CharMatcher.is('a').removeFrom(str);
        //移除str中的a
        CharMatcher.isNot('a').retainFrom(str);
        //保留str中的a,b,c字符
        CharMatcher.anyOf("abc").retainFrom(str);

        //保留str中的a,b,c字符
        CharMatcher.noneOf("abc").removeFrom(str);
        //匹配str中的a-j的字母，全部替换成数字6
        CharMatcher.inRange('a', 'j').replaceFrom(str, "6");
        //去str中的空格
        CharMatcher.breakingWhitespace().removeFrom(str);
        //去掉str中的数字
        CharMatcher.digit().removeFrom(str);
        //去掉控制字符(\t,\n,\b...)
        CharMatcher.javaIsoControl().removeFrom(str);
        //获取str中的小写字母
        CharMatcher.javaLowerCase().retainFrom(str);
        //获取str中的大写字母
        CharMatcher.javaUpperCase().retainFrom(str);

        //组合条件：获取str中的大写字母和数字
        System.out.println(CharMatcher.javaUpperCase().or(CharMatcher.digit()).retainFrom(str));

    }
}
