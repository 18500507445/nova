package com.nova.common.utils;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

/**
 * @Classname: NumberEncrypt
 * @Description: Number 加密处理器
 * @Date: 2022/4/2 9:48
 * @Created: by wangzehuit
 */
@Data
@Slf4j
public class NumberEncrypt {
    //转换字符（0-9分别为["D","e","C","A","P","b","J","I","z","M"]
    private final static String CONVERT_KEY = "DeCAPbJIzM";
    //混淆字母
    private final static String CONFUSED_WORDS_KEY = "FxYNgqZmOLT";
    //总加密字符串长度
    private final static int LEN_KEY = 32;

    private static BigInteger secretKey = new BigInteger("16000771");
    private static BigInteger confuseKey = new BigInteger("16600771");

    public static byte[] encode(byte[] content) {
        String str = new String(content);
        if (!NumberUtil.isNumber(str)) {
            return str.getBytes();
        }
        BigInteger number = new BigInteger(str);
        BigInteger newNumber = (number.add(confuseKey)).multiply(secretKey);
        String[] numArr = String.valueOf(newNumber).split("");
        String[] initArr = CONVERT_KEY.split("");
        int len = numArr.length;
        StringBuffer buffer = new StringBuffer();
        //数字转字母
        for (int i = 0; i < len; i++) {
            int inx = Integer.parseInt(numArr[i]);
            buffer.append(initArr[inx]);
        }
        //随机加入混淆字符
        String[] cwkArr = CONFUSED_WORDS_KEY.split("");
        if (len < LEN_KEY) {
            int l = LEN_KEY - len;
            for (int i = 0; i < l; i++) {
                int index = (int) (Math.random() * buffer.length());
                int inx = (int) (Math.random() * (CONFUSED_WORDS_KEY.length()));
                buffer.insert(index, cwkArr[inx]);
            }
        }
        String result = buffer.toString();
        return result.getBytes();
    }

    public static byte[] decode(byte[] content) {
        String str = new String(content);
        if (null == str || "".equals(str)) {
            return str.getBytes();
        }
        int l = CONFUSED_WORDS_KEY.length();
        String[] cwkArr = CONFUSED_WORDS_KEY.split("");
        for (int i = 0; i < l; i++) {
            str = str.replaceAll(cwkArr[i], "");
        }
        String[] initArr = str.split("");
        int len = initArr.length;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int k = CONVERT_KEY.indexOf(initArr[i]);
            if (k == -1) {
                return str.getBytes();
            }
            result.append(k);
        }
        BigInteger number;
        try {
            BigInteger total = new BigInteger(result.toString());
            BigInteger sum = total.divide(secretKey);
            number = sum.subtract(confuseKey);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return str.getBytes();
        }
        return number.toString().getBytes();
    }

    public static void main(String[] args) {
        String str = new String(NumberEncrypt.encode(new String("1508770920628756480").getBytes()));
        System.out.println(str);
        String str2 = new String(NumberEncrypt.decode("CxPxCNPMeOPCbMJeAIYAzzJPbbeNePMI".getBytes()));
        System.out.println(str2);
    }
}
