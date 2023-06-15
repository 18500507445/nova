package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharUtil;
import org.junit.jupiter.api.Test;

/**
 * {@link CharUtil} ClassLoader工具类 测试类
 *
 * @author
 */
public class CharUtilTest {

    @Test
    public void trimTest() {
        //此字符串中的第一个字符为不可见字符: '\u202a'
        final String str = "‪C:/Users/maple/Desktop/tone.txt";
        Assert.equals('\u202a', str.charAt(0));
        Assert.isTrue(CharUtil.isBlankChar(str.charAt(0)));
    }

    @Test
    public void isEmojiTest() {
        final String a = "莉🌹";
        Assert.isFalse(CharUtil.isEmoji(a.charAt(0)));
        Assert.isTrue(CharUtil.isEmoji(a.charAt(1)));

    }

    @Test
    public void isCharTest() {
        final char a = 'a';
        Assert.isTrue(CharUtil.isChar(a));
    }

    @Test
    public void isBlankCharTest() {
        final char a = '\u00A0';
        Assert.isTrue(CharUtil.isBlankChar(a));

        final char a2 = '\u0020';
        Assert.isTrue(CharUtil.isBlankChar(a2));

        final char a3 = '\u3000';
        Assert.isTrue(CharUtil.isBlankChar(a3));

        final char a4 = '\u0000';
        Assert.isTrue(CharUtil.isBlankChar(a4));
    }

    @Test
    public void toCloseCharTest() {
        Assert.equals('②', CharUtil.toCloseChar('2'));
        Assert.equals('Ⓜ', CharUtil.toCloseChar('M'));
        Assert.equals('ⓡ', CharUtil.toCloseChar('r'));
    }

    @Test
    public void toCloseByNumberTest() {
        Assert.equals('②', CharUtil.toCloseByNumber(2));
        Assert.equals('⑫', CharUtil.toCloseByNumber(12));
        Assert.equals('⑳', CharUtil.toCloseByNumber(20));
    }

    @Test
    public void issueI5UGSQTest() {
        char c = '\u3164';
        Assert.isTrue(CharUtil.isBlankChar(c));

        c = '\u2800';
        Assert.isTrue(CharUtil.isBlankChar(c));
    }
}
