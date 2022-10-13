package com.nova.common.utils.string;

import java.math.BigDecimal;

/**
 * @Description: 字符串间隔下标插入
 * @Author: wangzehui
 * @Date: 2020/12/24 15:37
 */

public class AddIndexStringUtil {

    public static final String SPACE = "&#13";

    public static final int LENGTH = 12;

    public static void main(String[] args) {
        String srr = "abcdefghijklmnopqrstuvwsyz";
        System.out.println(insertStr(srr));
    }

    public static String insertStr(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        BigDecimal num = new BigDecimal(str.length()).divide(new BigDecimal(LENGTH), 0, BigDecimal.ROUND_FLOOR);
        if (num.compareTo(BigDecimal.ZERO) > 0) {
            for (int i = 1; i <= num.intValue(); i++) {
                sb.insert(i * LENGTH, SPACE);
            }
        }
        return sb.toString();
    }
}
