package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * {@link HexUtil} 单元测试
 *
 * @author
 */
public class HexUtilTest {

    @Test
    public void hexStrTest() {
        String str = "我是一个字符串";

        String hex = HexUtil.encodeHexStr(str, CharsetUtil.CHARSET_UTF_8);
        String decodedStr = HexUtil.decodeHexStr(hex);

        Assert.equals(str, decodedStr);
    }

    @Test
    public void issueI50MI6Test() {
        String s = HexUtil.encodeHexStr("烟".getBytes(StandardCharsets.UTF_16BE));
        Assert.equals("70df", s);
    }

    @Test
    public void toUnicodeHexTest() {
        String unicodeHex = HexUtil.toUnicodeHex('\u2001');
        Assert.equals("\\u2001", unicodeHex);

        unicodeHex = HexUtil.toUnicodeHex('你');
        Assert.equals("\\u4f60", unicodeHex);
    }

    @Test
    public void isHexNumberTest() {
        String a = "0x3544534F444";
        boolean isHex = HexUtil.isHexNumber(a);
        Assert.isTrue(isHex);
    }

    @Test
    public void decodeTest() {
        String str = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
        Assert.equals(HexUtil.decodeHex(str),
                HexUtil.decodeHex(str.toUpperCase()));
    }

    @Test
    public void formatHexTest() {
        String hex = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
        String formatHex = HexUtil.format(hex);
        Assert.equals("e8 c6 70 38 0c b2 20 09 52 68 f4 02 21 fc 74 8f a6 ac 39 d6 e9 30 e6 3c 30 da 68 ba d9 7f 88 5d", formatHex);
    }

    @Test
    public void decodeHexTest() {
        String s = HexUtil.encodeHexStr("6");
        final String s1 = HexUtil.decodeHexStr(s);
        Assert.equals("6", s1);
    }
}
