package com.nova.common.utils.string;

import java.util.regex.Pattern;

/**
 * @description:
 * @author: wzh
 * @date: 2020/4/21 14:18
 */

public class ReplaceString {

    private static final String REGEX_ALL_BRACKETS = "\\<.*?\\>|\\(.*?\\)|\\（.*?\\）|\\[.*?\\]|\\【.*?\\】|\\{.*?\\}";

    public static String replace(String strHtml) {
        //读出body内里所有内容
        String strClear = strHtml.replaceAll(".*?<body.*?>(.*?)</body>", "$1");

        //保留br标签
        strClear = strClear.replaceAll("</?[^/?(br)][^><]*>", "");

        //去掉空白字符和&nbsp
        return strClear.replaceAll("\\s*", "").replaceAll("&nbsp;", "");
    }

    public static void main(String[] args) {
        //替换数字
        String a = "剩余10次";
        String replace = Pattern.compile("[^0-9]").matcher(a).replaceAll("").trim();
        String s = a.replaceAll(replace, "#" + replace + "#");
        System.out.println(s);

        //替换()
        String matchContent = "测试替换()内容了";
        matchContent = matchContent.replaceAll(REGEX_ALL_BRACKETS, "(" + 123 + ")");
        System.out.println(matchContent);
    }
}
