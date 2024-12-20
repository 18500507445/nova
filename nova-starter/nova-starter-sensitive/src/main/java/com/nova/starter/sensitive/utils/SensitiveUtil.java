package com.nova.starter.sensitive.utils;

import cn.hutool.core.util.StrUtil;
import com.nova.starter.sensitive.StringPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @description: 脱敏工具类，也可以用hutool的DesensitizedUtil
 * @author: wzh
 * @date: 2024/3/8 13:30
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SensitiveUtil {

    /**
     * [手机号码] 前3位后4位明码，中间4位掩码用****显示，如138****0000
     *
     * @param mobile 手机号码
     */
    public static String handlerMobile(String mobile) {
        if (StrUtil.isEmpty(mobile)) {
            return null;
        }
        return hide(mobile, 3, mobile.length() - 4);
    }

    /**
     * [手机号码] 只显示后四位, 如:*8856
     *
     * @param phone 手机号码
     */
    public static String handlerPhone(String phone) {
        if (StrUtil.isEmpty(phone)) {
            return null;
        }
        return overlay(phone, StringPool.ASTERISK, 1, 0, phone.length() - 4);
    }


    /**
     * [身份证号] 前6位后4位明码，中间掩码用***显示，如511623********0537
     *
     * @param idNum
     */
    public static String handlerIdCard(String idNum) {
        if (StrUtil.isEmpty(idNum)) {
            return null;
        }
        return hide(idNum, 6, idNum.length() - 4);
    }

    /**
     * [银行卡] 前6位后4位明码，中间部分****代替，如622848*********5579
     *
     * @param cardNum
     */
    public static String handlerBankCard(String cardNum) {
        return handlerIdCard(cardNum);
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：广东省广州市天河区****>
     *
     * @param address
     */
    public static String handlerAddress(String address) {
        if (StrUtil.isEmpty(address)) {
            return null;
        }
        return overlay(address, StringPool.ASTERISK, 4, 9, address.length());
    }


    /**
     * [用户名] 只显示第一位 <例子：黄**>
     *
     * @param username
     */
    public static String handlerUsername(String username) {
        if (StrUtil.isEmpty(username)) {
            return null;
        }
        return hide(username, 1, username.length());
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     */
    public static String handlerFixedPhone(final String num) {
        if (StrUtil.isEmpty(num)) {
            return null;
        }
        return overlay(num, StringPool.ASTERISK, 4, 0, num.length() - 4);
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     *
     * @param email
     */
    public static String handlerEmail(final String email) {
        if (StrUtil.isEmpty(email)) {
            return null;
        }
        final int index = StrUtil.indexOf(email, StringPool.AT_CHAR);
        if (index <= 1) {
            return email;
        } else {
            return hide(email, 1, index);
        }
    }

    /**
     * 用另一个字符串覆盖一个字符串的一部分
     *
     * @param str           需要替换的字符串
     * @param overlay       将被替换成的字符串
     * @param overlayRepeat overlay重复的次数
     * @param start         开始位置
     * @param end           结束位置
     */
    public static String overlay(String str, String overlay, int overlayRepeat, int start, int end) {
        if (StrUtil.isEmpty(str)) {
            return StrUtil.str(str);
        }
        final int len = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > len) {
            start = len;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > len) {
            end = len;
        }
        if (start > end) {
            final int temp = start;
            start = end;
            end = temp;
        }
        return str.substring(0, start) + StrUtil.repeat(overlay, overlayRepeat) + str.substring(end);
    }

    /**
     * 替换指定字符串的指定区间内字符为"*"
     *
     * @param str          字符串
     * @param startInclude 开始位置（包含）
     * @param endExclude   结束位置（不包含）
     *                     替换后的字符串
     */
    public static String hide(String str, int startInclude, int endExclude) {
        return hide(str, startInclude, endExclude, StringPool.ASTERISK_CHAR);
    }

    /**
     * 替换指定字符串的指定区间内字符为"*"
     *
     * @param str          字符串
     * @param startInclude 开始位置（包含）
     * @param endExclude   结束位置（不包含）
     * @param replacedChar 替换后的字符串
     */
    public static String hide(String str, int startInclude, int endExclude, char replacedChar) {
        if (StrUtil.isEmpty(str)) {
            return StrUtil.str(str);
        }
        final int strLength = str.length();
        if (startInclude > strLength) {
            return StrUtil.str(str);
        }
        if (endExclude > strLength) {
            endExclude = strLength;
        }
        if (startInclude > endExclude) {
            // 如果起始位置大于结束位置，不替换
            return StrUtil.str(str);
        }
        final char[] chars = new char[strLength];
        for (int i = 0; i < strLength; i++) {
            if (i >= startInclude && i < endExclude) {
                chars[i] = replacedChar;
            } else {
                chars[i] = str.charAt(i);
            }
        }
        return new String(chars);
    }

}
