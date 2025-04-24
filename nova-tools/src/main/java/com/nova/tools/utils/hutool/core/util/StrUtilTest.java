package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * {@link StrUtil}
 * 字符串工具类单元测试
 *
 * @author:Looly
 */
public class StrUtilTest {

    @Test
    public void isBlankTest() {
        String blank = "	  　";
        Assert.isTrue(StrUtil.isBlank(blank));
    }

    @Test
    public void trimTest() {
        String blank = "	 哈哈 　";
        String trim = StrUtil.trim(blank);
        Assert.equals("哈哈", trim);
    }

    @Test
    public void trimNewLineTest() {
        String str = "\r\naaa";
        Assert.equals("aaa", StrUtil.trim(str));
        str = "\raaa";
        Assert.equals("aaa", StrUtil.trim(str));
        str = "\naaa";
        Assert.equals("aaa", StrUtil.trim(str));
        str = "\r\n\r\naaa";
        Assert.equals("aaa", StrUtil.trim(str));
    }

    @Test
    public void trimTabTest() {
        String str = "\taaa";
        Assert.equals("aaa", StrUtil.trim(str));
    }

    @Test
    public void cleanBlankTest() {
        // 包含：制表符、英文空格、不间断空白符、全角空格
        String str = "	 你 好　";
        String cleanBlank = StrUtil.cleanBlank(str);
        Assert.equals("你好", cleanBlank);
    }

    @Test
    public void cutTest() {
        String str = "aaabbbcccdddaadfdfsdfsdf0";
        String[] cut = StrUtil.cut(str, 4);
        Assert.equals(new String[]{"aaab", "bbcc", "cddd", "aadf", "dfsd", "fsdf", "0"}, cut);
    }

    @Test
    public void splitTest() {
        String str = "a,b ,c,d,,e";
        List<String> split = StrUtil.split(str, ',', -1, true, true);
        // 测试空是否被去掉
        Assert.equals(5, split.size());
        // 测试去掉两边空白符是否生效
        Assert.equals("b", split.get(1));

        final String[] strings = StrUtil.splitToArray("abc/", '/');
        Assert.equals(2, strings.length);
    }

    @Test
    public void splitEmptyTest() {
        String str = "";
        List<String> split = StrUtil.split(str, ',', -1, true, true);
        // 测试空是否被去掉
        Assert.equals(0, split.size());
    }

    @Test
    public void splitTest2() {
        String str = "a.b.";
        List<String> split = StrUtil.split(str, '.');
        Assert.equals(3, split.size());
        Assert.equals("b", split.get(1));
        Assert.equals("", split.get(2));
    }

    @Test
    public void splitNullTest() {
        Assert.equals(0, StrUtil.split(null, '.').size());
    }

    @Test
    public void splitToArrayNullTest() {
        StrUtil.splitToArray(null, '.');
    }

    @Test
    public void splitToLongTest() {
        String str = "1,2,3,4, 5";
        long[] longArray = StrUtil.splitToLong(str, ',');
        Assert.equals(new long[]{1, 2, 3, 4, 5}, longArray);

        longArray = StrUtil.splitToLong(str, ",");
        Assert.equals(new long[]{1, 2, 3, 4, 5}, longArray);
    }

    @Test
    public void splitToIntTest() {
        String str = "1,2,3,4, 5";
        int[] intArray = StrUtil.splitToInt(str, ',');
        Assert.equals(new int[]{1, 2, 3, 4, 5}, intArray);

        intArray = StrUtil.splitToInt(str, ",");
        Assert.equals(new int[]{1, 2, 3, 4, 5}, intArray);
    }

    @Test
    public void formatTest() {
        String template = "你好，我是{name}，我的电话是：{phone}";
        String result = StrUtil.format(template, Dict.create().set("name", "张三").set("phone", "13888881111"));
        Assert.equals("你好，我是张三，我的电话是：13888881111", result);

        String result2 = StrUtil.format(template, Dict.create().set("name", "张三").set("phone", null));
        Assert.equals("你好，我是张三，我的电话是：{phone}", result2);
    }

    @Test
    public void stripTest() {
        String str = "abcd123";
        String strip = StrUtil.strip(str, "ab", "23");
        Assert.equals("cd1", strip);

        str = "abcd123";
        strip = StrUtil.strip(str, "ab", "");
        Assert.equals("cd123", strip);

        str = "abcd123";
        strip = StrUtil.strip(str, null, "");
        Assert.equals("abcd123", strip);

        str = "abcd123";
        strip = StrUtil.strip(str, null, "567");
        Assert.equals("abcd123", strip);

        Assert.equals("", StrUtil.strip("a", "a"));
        Assert.equals("", StrUtil.strip("a", "a", "b"));
    }

    @Test
    public void stripIgnoreCaseTest() {
        String str = "abcd123";
        String strip = StrUtil.stripIgnoreCase(str, "Ab", "23");
        Assert.equals("cd1", strip);

        str = "abcd123";
        strip = StrUtil.stripIgnoreCase(str, "AB", "");
        Assert.equals("cd123", strip);

        str = "abcd123";
        strip = StrUtil.stripIgnoreCase(str, "ab", "");
        Assert.equals("cd123", strip);

        str = "abcd123";
        strip = StrUtil.stripIgnoreCase(str, null, "");
        Assert.equals("abcd123", strip);

        str = "abcd123";
        strip = StrUtil.stripIgnoreCase(str, null, "567");
        Assert.equals("abcd123", strip);
    }

    @Test
    public void indexOfIgnoreCaseTest() {
        Assert.equals(-1, StrUtil.indexOfIgnoreCase(null, "balabala", 0));
        Assert.equals(-1, StrUtil.indexOfIgnoreCase("balabala", null, 0));
        Assert.equals(0, StrUtil.indexOfIgnoreCase("", "", 0));
        Assert.equals(0, StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0));
        Assert.equals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0));
        Assert.equals(1, StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0));
        Assert.equals(5, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3));
        Assert.equals(-1, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9));
        Assert.equals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1));
        Assert.equals(-1, StrUtil.indexOfIgnoreCase("aabaabaa", "", 2));
        Assert.equals(-1, StrUtil.indexOfIgnoreCase("abc", "", 9));
    }

    @Test
    public void lastIndexOfTest() {
        String a = "aabbccddcc";
        int lastIndexOf = StrUtil.lastIndexOf(a, "c", 0, false);
        Assert.equals(-1, lastIndexOf);
    }

    @Test
    public void lastIndexOfIgnoreCaseTest() {
        Assert.equals(-1, StrUtil.lastIndexOfIgnoreCase(null, "balabala", 0));
        Assert.equals(-1, StrUtil.lastIndexOfIgnoreCase("balabala", null));
        Assert.equals(0, StrUtil.lastIndexOfIgnoreCase("", ""));
        Assert.equals(7, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "A"));
        Assert.equals(5, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B"));
        Assert.equals(4, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "AB"));
        Assert.equals(2, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 3));
        Assert.equals(5, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 9));
        Assert.equals(-1, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", -1));
        Assert.equals(-1, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "", 2));
        Assert.equals(-1, StrUtil.lastIndexOfIgnoreCase("abc", "", 9));
        Assert.equals(0, StrUtil.lastIndexOfIgnoreCase("AAAcsd", "aaa"));
    }

    @Test
    public void replaceTest() {
        String string = StrUtil.replace("aabbccdd", 2, 6, '*');
        Assert.equals("aa****dd", string);
        string = StrUtil.replace("aabbccdd", 2, 12, '*');
        Assert.equals("aa******", string);
    }

    @Test
    public void replaceTest2() {
        String result = StrUtil.replace("123", "2", "3");
        Assert.equals("133", result);
    }

    @Test
    public void replaceTest3() {
        String result = StrUtil.replace(",abcdef,", ",", "|");
        Assert.equals("|abcdef|", result);
    }

    @Test
    public void replaceTest4() {
        String a = "1039";
        String result = StrUtil.padPre(a, 8, "0"); //在字符串1039前补4个0
        Assert.equals("00001039", result);

        String aa = "1039";
        String result1 = StrUtil.padPre(aa, -1, "0"); //在字符串1039前补4个0
        Assert.equals("103", result1);
    }

    @Test
    public void replaceTest5() {
        String a = "\uD853\uDC09秀秀";
        String result = StrUtil.replace(a, 1, a.length(), '*');
        Assert.equals("\uD853\uDC09**", result);

        String aa = "规划大师";
        String result1 = StrUtil.replace(aa, 2, a.length(), '*');
        Assert.equals("规划**", result1);
    }

    @Test
    public void upperFirstTest() {
        StringBuilder sb = new StringBuilder("KEY");
        String s = StrUtil.upperFirst(sb);
        Assert.equals(s, sb.toString());
    }

    @Test
    public void lowerFirstTest() {
        StringBuilder sb = new StringBuilder("KEY");
        String s = StrUtil.lowerFirst(sb);
        Assert.equals("kEY", s);
    }

    @Test
    public void subTest() {
        String a = "abcderghigh";
        String pre = StrUtil.sub(a, -5, a.length());
        Assert.equals("ghigh", pre);
    }

    @Test
    public void subByCodePointTest() {
        // 🤔👍🍓🤔
        String test = "\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53\uD83E\uDD14";

        // 不正确的子字符串
        String wrongAnswer = StrUtil.sub(test, 0, 3);
        Assert.notEquals("\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53", wrongAnswer);

        // 正确的子字符串
        String rightAnswer = StrUtil.subByCodePoint(test, 0, 3);
        Assert.equals("\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53", rightAnswer);
    }

    @Test
    public void subBeforeTest() {
        String a = "abcderghigh";
        String pre = StrUtil.subBefore(a, "d", false);
        Assert.equals("abc", pre);
        pre = StrUtil.subBefore(a, 'd', false);
        Assert.equals("abc", pre);
        pre = StrUtil.subBefore(a, 'a', false);
        Assert.equals("", pre);

        //找不到返回原串
        pre = StrUtil.subBefore(a, 'k', false);
        Assert.equals(a, pre);
        pre = StrUtil.subBefore(a, 'k', true);
        Assert.equals(a, pre);
    }

    @Test
    public void subAfterTest() {
        String a = "abcderghigh";
        String pre = StrUtil.subAfter(a, "d", false);
        Assert.equals("erghigh", pre);
        pre = StrUtil.subAfter(a, 'd', false);
        Assert.equals("erghigh", pre);
        pre = StrUtil.subAfter(a, 'h', true);
        Assert.equals("", pre);

        //找不到字符返回空串
        pre = StrUtil.subAfter(a, 'k', false);
        Assert.equals("", pre);
        pre = StrUtil.subAfter(a, 'k', true);
        Assert.equals("", pre);
    }

    @Test
    public void subSufByLengthTest() {
        Assert.equals("cde", StrUtil.subSufByLength("abcde", 3));
        Assert.equals("", StrUtil.subSufByLength("abcde", -1));
        Assert.equals("", StrUtil.subSufByLength("abcde", 0));
        Assert.equals("abcde", StrUtil.subSufByLength("abcde", 5));
        Assert.equals("abcde", StrUtil.subSufByLength("abcde", 10));
    }

    @Test
    public void repeatAndJoinTest() {
        String repeatAndJoin = StrUtil.repeatAndJoin("?", 5, ",");
        Assert.equals("?,?,?,?,?", repeatAndJoin);

        repeatAndJoin = StrUtil.repeatAndJoin("?", 0, ",");
        Assert.equals("", repeatAndJoin);

        repeatAndJoin = StrUtil.repeatAndJoin("?", 5, null);
        Assert.equals("?????", repeatAndJoin);
    }

    @Test
    public void moveTest() {
        String str = "aaaaaaa22222bbbbbbb";
        String result = StrUtil.move(str, 7, 12, -3);
        Assert.equals("aaaa22222aaabbbbbbb", result);
        result = StrUtil.move(str, 7, 12, -4);
        Assert.equals("aaa22222aaaabbbbbbb", result);
        result = StrUtil.move(str, 7, 12, -7);
        Assert.equals("22222aaaaaaabbbbbbb", result);
        result = StrUtil.move(str, 7, 12, -20);
        Assert.equals("aaaaaa22222abbbbbbb", result);

        result = StrUtil.move(str, 7, 12, 3);
        Assert.equals("aaaaaaabbb22222bbbb", result);
        result = StrUtil.move(str, 7, 12, 7);
        Assert.equals("aaaaaaabbbbbbb22222", result);
        result = StrUtil.move(str, 7, 12, 20);
        Assert.equals("aaaaaaab22222bbbbbb", result);

        result = StrUtil.move(str, 7, 12, 0);
        Assert.equals("aaaaaaa22222bbbbbbb", result);
    }

    @Test
    public void removePrefixIgnorecaseTest() {
        String a = "aaabbb";
        String prefix = "aaa";
        Assert.equals("bbb", StrUtil.removePrefixIgnoreCase(a, prefix));

        prefix = "AAA";
        Assert.equals("bbb", StrUtil.removePrefixIgnoreCase(a, prefix));

        prefix = "AAABBB";
        Assert.equals("", StrUtil.removePrefixIgnoreCase(a, prefix));
    }

    @Test
    public void maxLengthTest() {
        String text = "我是一段正文，很长的正文，需要截取的正文";
        String str = StrUtil.maxLength(text, 5);
        Assert.equals("我是一段正...", str);
        str = StrUtil.maxLength(text, 21);
        Assert.equals(text, str);
        str = StrUtil.maxLength(text, 50);
        Assert.equals(text, str);
    }

    @Test
    public void containsAnyTest() {
        //字符
        boolean containsAny = StrUtil.containsAny("aaabbbccc", 'a', 'd');
        Assert.isTrue(containsAny);
        containsAny = StrUtil.containsAny("aaabbbccc", 'e', 'd');
        Assert.isFalse(containsAny);
        containsAny = StrUtil.containsAny("aaabbbccc", 'd', 'c');
        Assert.isTrue(containsAny);

        //字符串
        containsAny = StrUtil.containsAny("aaabbbccc", "a", "d");
        Assert.isTrue(containsAny);
        containsAny = StrUtil.containsAny("aaabbbccc", "e", "d");
        Assert.isFalse(containsAny);
        containsAny = StrUtil.containsAny("aaabbbccc", "d", "c");
        Assert.isTrue(containsAny);
    }

    @Test
    public void centerTest() {
        Assert.isNull(StrUtil.center(null, 10));
        Assert.equals("    ", StrUtil.center("", 4));
        Assert.equals("ab", StrUtil.center("ab", -1));
        Assert.equals(" ab ", StrUtil.center("ab", 4));
        Assert.equals("abcd", StrUtil.center("abcd", 2));
        Assert.equals(" a  ", StrUtil.center("a", 4));
    }

    @Test
    public void padPreTest() {
        Assert.isNull(StrUtil.padPre(null, 10, ' '));
        Assert.equals("001", StrUtil.padPre("1", 3, '0'));
        Assert.equals("12", StrUtil.padPre("123", 2, '0'));

        Assert.isNull(StrUtil.padPre(null, 10, "AA"));
        Assert.equals("AB1", StrUtil.padPre("1", 3, "ABC"));
        Assert.equals("12", StrUtil.padPre("123", 2, "ABC"));
    }

    @Test
    public void padAfterTest() {
        Assert.isNull(StrUtil.padAfter(null, 10, ' '));
        Assert.equals("100", StrUtil.padAfter("1", 3, '0'));
        Assert.equals("23", StrUtil.padAfter("123", 2, '0'));
        Assert.equals("", StrUtil.padAfter("123", -1, '0'));

        Assert.isNull(StrUtil.padAfter(null, 10, "ABC"));
        Assert.equals("1AB", StrUtil.padAfter("1", 3, "ABC"));
        Assert.equals("23", StrUtil.padAfter("123", 2, "ABC"));
    }

    @Test
    public void subBetweenAllTest() {
        Assert.equals(new String[]{"yz", "abc"}, StrUtil.subBetweenAll("saho[yz]fdsadp[abc]a", "[", "]"));
        Assert.equals(new String[]{"abc"}, StrUtil.subBetweenAll("saho[yzfdsadp[abc]a]", "[", "]"));
        Assert.equals(new String[]{"abc", "abc"}, StrUtil.subBetweenAll("yabczyabcz", "y", "z"));
        Assert.equals(new String[0], StrUtil.subBetweenAll(null, "y", "z"));
        Assert.equals(new String[0], StrUtil.subBetweenAll("", "y", "z"));
        Assert.equals(new String[0], StrUtil.subBetweenAll("abc", null, "z"));
        Assert.equals(new String[0], StrUtil.subBetweenAll("abc", "y", null));
    }

    @Test
    public void subBetweenAllTest2() {
        //issue#861@Github，起始不匹配的时候，应该直接空
        String src1 = "/* \n* hutool  */  asdas  /* \n* hutool  */";
        String src2 = "/ * hutool  */  asdas  / * hutool  */";

        String[] results1 = StrUtil.subBetweenAll(src1, "/**", "*/");
        Assert.equals(0, results1.length);

        String[] results2 = StrUtil.subBetweenAll(src2, "/*", "*/");
        Assert.equals(0, results2.length);
    }

    @Test
    public void subBetweenAllTest3() {
        String src1 = "'abc'and'123'";
        String[] strings = StrUtil.subBetweenAll(src1, "'", "'");
        Assert.equals(2, strings.length);
        Assert.equals("abc", strings[0]);
        Assert.equals("123", strings[1]);

        String src2 = "'abc''123'";
        strings = StrUtil.subBetweenAll(src2, "'", "'");
        Assert.equals(2, strings.length);
        Assert.equals("abc", strings[0]);
        Assert.equals("123", strings[1]);

        String src3 = "'abc'123'";
        strings = StrUtil.subBetweenAll(src3, "'", "'");
        Assert.equals(1, strings.length);
        Assert.equals("abc", strings[0]);
    }

    @Test
    public void subBetweenAllTest4() {
        String str = "你好:1388681xxxx用户已开通,1877275xxxx用户已开通,无法发送业务开通短信";
        String[] strings = StrUtil.subBetweenAll(str, "1877275xxxx", ",");
        Assert.equals(1, strings.length);
        Assert.equals("用户已开通", strings[0]);
    }

    @Test
    public void briefTest() {
        // case: 1 至 str.length - 1
        String str = RandomUtil.randomString(RandomUtil.randomInt(1, 100));
        for (int maxLength = 1; maxLength < str.length(); maxLength++) {
            String brief = StrUtil.brief(str, maxLength);
            Assert.equals(brief.length(), maxLength);
        }

        // case: 不会格式化的值
        Assert.equals(str, StrUtil.brief(str, 0));
        Assert.equals(str, StrUtil.brief(str, -1));
        Assert.equals(str, StrUtil.brief(str, str.length()));
        Assert.equals(str, StrUtil.brief(str, str.length() + 1));
    }

    @Test
    public void briefTest2() {
        String str = "123";
        int maxLength = 3;
        String brief = StrUtil.brief(str, maxLength);
        Assert.equals("123", brief);

        maxLength = 2;
        brief = StrUtil.brief(str, maxLength);
        Assert.equals("1.", brief);

        maxLength = 1;
        brief = StrUtil.brief(str, maxLength);
        Assert.equals("1", brief);
    }

    @Test
    public void briefTest3() {
        String str = "123abc";

        int maxLength = 6;
        String brief = StrUtil.brief(str, maxLength);
        Assert.equals(str, brief);

        maxLength = 5;
        brief = StrUtil.brief(str, maxLength);
        Assert.equals("1...c", brief);

        maxLength = 4;
        brief = StrUtil.brief(str, maxLength);
        Assert.equals("1..c", brief);

        maxLength = 3;
        brief = StrUtil.brief(str, maxLength);
        Assert.equals("1.c", brief);

        maxLength = 2;
        brief = StrUtil.brief(str, maxLength);
        Assert.equals("1.", brief);

        maxLength = 1;
        brief = StrUtil.brief(str, maxLength);
        Assert.equals("1", brief);
    }

    @Test
    public void filterTest() {
        final String filterNumber = StrUtil.filter("hutool678", CharUtil::isNumber);
        Assert.equals("678", filterNumber);
        String cleanBlank = StrUtil.filter("	 你 好　", c -> !CharUtil.isBlankChar(c));
        Assert.equals("你好", cleanBlank);
    }

    @Test
    public void wrapAllTest() {
        String[] strings = StrUtil.wrapAll("`", "`", StrUtil.splitToArray("1,2,3,4", ','));
        Assert.equals("[`1`, `2`, `3`, `4`]", StrUtil.utf8Str(strings));

        strings = StrUtil.wrapAllWithPair("`", StrUtil.splitToArray("1,2,3,4", ','));
        Assert.equals("[`1`, `2`, `3`, `4`]", StrUtil.utf8Str(strings));
    }

    @Test
    public void startWithTest() {
        String a = "123";
        String b = "123";

        Assert.isTrue(StrUtil.startWith(a, b));
        Assert.isFalse(StrUtil.startWithIgnoreEquals(a, b));
    }

    @Test
    public void indexedFormatTest() {
        final String ret = StrUtil.indexedFormat("this is {0} for {1}", "a", 1000);
        Assert.equals("this is a for 1,000", ret);
    }

    @Test
    public void hideTest() {
        Assert.isNull(StrUtil.hide(null, 1, 1));
        Assert.equals("", StrUtil.hide("", 1, 1));
        Assert.equals("****duan@163.com", StrUtil.hide("jackduan@163.com", -1, 4));
        Assert.equals("ja*kduan@163.com", StrUtil.hide("jackduan@163.com", 2, 3));
        Assert.equals("jackduan@163.com", StrUtil.hide("jackduan@163.com", 3, 2));
        Assert.equals("jackduan@163.com", StrUtil.hide("jackduan@163.com", 16, 16));
        Assert.equals("jackduan@163.com", StrUtil.hide("jackduan@163.com", 16, 17));
    }


    @Test
    public void isCharEqualsTest() {
        String a = "aaaaaaaaa";
        Assert.isTrue(StrUtil.isCharEquals(a));
    }

    @Test
    public void isNumericTest() {
        String a = "2142342422423423";
        Assert.isTrue(StrUtil.isNumeric(a));
    }

    @Test
    public void containsAllTest() {
        String a = "2142342422423423";
        Assert.isTrue(StrUtil.containsAll(a, "214", "234"));
    }

    @Test
    public void replaceLastTest() {
        String str = "i am jackjack";
        String result = StrUtil.replaceLast(str, "JACK", null, true);
        Assert.equals(result, "i am jack");
    }

    @Test
    public void replaceFirstTest() {
        String str = "yesyes i do";
        String result = StrUtil.replaceFirst(str, "YES", "", true);
        Assert.equals(result, "yes i do");
    }
}
