package com.nova.tools.utils.string;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2020/4/21 14:18
 */

public class ReplaceString {

    public static String replace(String strHtml) {
        //读出body内里所有内容
        String strClear = strHtml.replaceAll(".*?<body.*?>(.*?)</body>", "$1");

        //保留br标签
        strClear = strClear.replaceAll("</?[^/?(br)][^><]*>", "");

        //去掉空白字符和&nbsp
        return strClear.replaceAll("\\s*", "").replaceAll("&nbsp;", "");
    }
}
