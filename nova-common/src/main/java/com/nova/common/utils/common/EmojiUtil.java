package com.nova.common.utils.common;

import cn.hutool.core.util.StrUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: emoji表情编码处理
 * @author: wzh
 * @date: 2022/8/4 21:29
 */
public class EmojiUtil {

    /**
     * @param str 待转换字符串
     * @return 转换后字符串
     * @throws UnsupportedEncodingException
     * @Description emoji表情转换
     */
    public static String emojiConvertToUtf(String str) {
        if (StrUtil.isBlank(str)) {
            return "";
        }
        try {
            String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(str);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                try {
                    matcher.appendReplacement(sb, "<emoji>" + URLEncoder.encode(matcher.group(1), "UTF-8") + "</emoji>");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            matcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * @param str 转换后的字符串
     * @return 转换前的字符串
     * @throws UnsupportedEncodingException
     * @Description 还原emoji表情的字符串
     */
    public static String utfemojiRecovery(String str) {
        if (StrUtil.isBlank(str)) {
            return "";
        }
        try {
            String patternString = "<emoji>(.*?)</emoji>";

            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(str);

            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                try {
                    matcher.appendReplacement(sb, URLDecoder.decode(matcher.group(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            matcher.appendTail(sb);
            return sb.toString().replaceAll("<emoji>", "").replaceAll("</emoji>", "");

        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static void main(String[] args) {
        System.err.println(EmojiUtil.utfemojiRecovery("哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈<emoji>%F0%9F%91%8C</emoji>"));
    }

}
