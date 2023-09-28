package com.nova.tools.utils.hutool.core.text;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class CharSequenceUtilTest {

    @Test
    public void replaceTest() {
        String actual = CharSequenceUtil.replace("SSM15930297701BeryAllen", Pattern.compile("[0-9]"), matcher -> "");
        Assert.equals("SSMBeryAllen", actual);
    }

    @Test
    public void replaceTest2() {
        // https://gitee.com/dromara/hutool/issues/I4M16G
        String replace = "#{A}";
        String result = CharSequenceUtil.replace(replace, "#{AAAAAAA}", "1");
        Assert.equals(replace, result);
    }

    @Test
    public void replaceByStrTest() {
        String replace = "SSM15930297701BeryAllen";
        String result = CharSequenceUtil.replace(replace, 5, 12, "***");
        Assert.equals("SSM15***01BeryAllen", result);
    }

    @Test
    public void addPrefixIfNotTest() {
        String str = "hutool";
        String result = CharSequenceUtil.addPrefixIfNot(str, "hu");
        Assert.equals(str, result);

        result = CharSequenceUtil.addPrefixIfNot(str, "Good");
        Assert.equals("Good" + str, result);
    }

    @Test
    public void addSuffixIfNotTest() {
        String str = "hutool";
        String result = CharSequenceUtil.addSuffixIfNot(str, "tool");
        Assert.equals(str, result);

        result = CharSequenceUtil.addSuffixIfNot(str, " is Good");
        Assert.equals(str + " is Good", result);

        // https://gitee.com/dromara/hutool/issues/I4NS0F
        result = CharSequenceUtil.addSuffixIfNot("", "/");
        Assert.equals("/", result);
    }

    @Test
    public void normalizeTest() {
        // https://blog.csdn.net/oscar999/article/details/105326270

        String str1 = "\u00C1";
        String str2 = "\u0041\u0301";

        Assert.notEquals(str1, str2);

        str1 = CharSequenceUtil.normalize(str1);
        str2 = CharSequenceUtil.normalize(str2);
        Assert.equals(str1, str2);
    }

    @Test
    public void indexOfTest() {
        int index = CharSequenceUtil.indexOf("abc123", '1');
        Assert.equals(3, index);
        index = CharSequenceUtil.indexOf("abc123", '3');
        Assert.equals(5, index);
        index = CharSequenceUtil.indexOf("abc123", 'a');
        Assert.equals(0, index);
    }

    @Test
    public void indexOfTest2() {
        int index = CharSequenceUtil.indexOf("abc123", '1', 0, 3);
        Assert.equals(-1, index);

        index = CharSequenceUtil.indexOf("abc123", 'b', 0, 3);
        Assert.equals(1, index);
    }

    @Test
    public void subPreGbkTest() {
        // https://gitee.com/dromara/hutool/issues/I4JO2E
        String s = "华硕K42Intel酷睿i31代2G以下独立显卡不含机械硬盘固态硬盘120GB-192GB4GB-6GB";

        String v = CharSequenceUtil.subPreGbk(s, 40, false);
        Assert.equals(39, v.getBytes(CharsetUtil.CHARSET_GBK).length);

        v = CharSequenceUtil.subPreGbk(s, 40, true);
        Assert.equals(41, v.getBytes(CharsetUtil.CHARSET_GBK).length);
    }

    @Test
    public void startWithTest() {
        // https://gitee.com/dromara/hutool/issues/I4MV7Q
        Assert.isFalse(CharSequenceUtil.startWith("123", "123", false, true));
        Assert.isFalse(CharSequenceUtil.startWith(null, null, false, true));
        Assert.isFalse(CharSequenceUtil.startWith("abc", "abc", true, true));

        Assert.isTrue(CharSequenceUtil.startWithIgnoreCase(null, null));
        Assert.isFalse(CharSequenceUtil.startWithIgnoreCase(null, "abc"));
        Assert.isFalse(CharSequenceUtil.startWithIgnoreCase("abcdef", null));
        Assert.isTrue(CharSequenceUtil.startWithIgnoreCase("abcdef", "abc"));
        Assert.isTrue(CharSequenceUtil.startWithIgnoreCase("ABCDEF", "abc"));
    }

    @Test
    public void endWithTest() {
        Assert.isFalse(CharSequenceUtil.endWith("123", "123", false, true));
        Assert.isFalse(CharSequenceUtil.endWith(null, null, false, true));
        Assert.isFalse(CharSequenceUtil.endWith("abc", "abc", true, true));

        Assert.isTrue(CharSequenceUtil.endWithIgnoreCase(null, null));
        Assert.isFalse(CharSequenceUtil.endWithIgnoreCase(null, "abc"));
        Assert.isFalse(CharSequenceUtil.endWithIgnoreCase("abcdef", null));
        Assert.isTrue(CharSequenceUtil.endWithIgnoreCase("abcdef", "def"));
        Assert.isTrue(CharSequenceUtil.endWithIgnoreCase("ABCDEF", "def"));
    }

    @Test
    public void removePrefixIgnoreCaseTest() {
        Assert.equals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "abc"));
        Assert.equals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABC"));
        Assert.equals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "Abc"));
        Assert.equals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", ""));
        Assert.equals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", null));
        Assert.equals("", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABCde"));
        Assert.equals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABCdef"));
        Assert.isNull(CharSequenceUtil.removePrefixIgnoreCase(null, "ABCdef"));
    }

    @Test
    public void removeSuffixIgnoreCaseTest() {
        Assert.equals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "cde"));
        Assert.equals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "CDE"));
        Assert.equals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "Cde"));
        Assert.equals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", ""));
        Assert.equals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", null));
        Assert.equals("", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "ABCde"));
        Assert.equals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "ABCdef"));
        Assert.isNull(CharSequenceUtil.removeSuffixIgnoreCase(null, "ABCdef"));
    }

    @Test
    public void trimToNullTest() {
        String a = "  ";
        Assert.isNull(CharSequenceUtil.trimToNull(a));

        a = "";
        Assert.isNull(CharSequenceUtil.trimToNull(a));

        a = null;
        Assert.isNull(CharSequenceUtil.trimToNull(a));
    }
}
