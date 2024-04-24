package com.nova.common.utils.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

/**
 * @author: wzh
 * @description: 唯一ID生成唯一邀请码
 * @date: 2022/5/16 17:48
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CodeUtils {

    /**
     * 自定义进制(0,1没有加入,容易与o,l混淆)
     */
    private static final char[] R = new char[]{'q', 'w', 'e', '8', 'a', 's', '2', 'd', 'z', 'x', '9', 'c', '7', 'p', '5', 'i', 'k', '3', 'm', 'j', 'u', 'f', 'r', '4', 'v', 'y', 'l', 't', 'n', '6', 'b', 'g', 'h'};

    /**
     * (不能与自定义进制有重复)
     */
    private static final char B = 'o';

    /**
     * 进制长度
     */
    private static final int BIN_LEN = R.length;

    /**
     * 序列长度
     */
    private static final int S = 4;

    /**
     * 根据ID生成六位随机码
     *
     * @param id ID
     * @return 随机码
     */
    public static String toSerialCode(long id) {
        char[] buf = new char[32];
        int charPos = 32;

        while ((id / BIN_LEN) > 0) {
            int ind = (int) (id % BIN_LEN);
            // System.err.println(num + "-->" + ind);
            buf[--charPos] = R[ind];
            id /= BIN_LEN;
        }
        buf[--charPos] = R[(int) (id % BIN_LEN)];
        // System.err.println(num + "-->" + num % binLen);
        String str = new String(buf, charPos, (32 - charPos));
        // 不够长度的自动随机补全
        if (str.length() < S) {
            StringBuilder sb = new StringBuilder();
            sb.append(B);
            Random rnd = new Random();
            for (int i = 1; i < S - str.length(); i++) {
                sb.append(R[rnd.nextInt(BIN_LEN)]);
            }
            str += sb.toString();
        }
        return str;
    }

    public static long codeToId(String code) {
        char chs[] = code.toCharArray();
        long res = 0L;
        for (int i = 0; i < chs.length; i++) {
            int ind = 0;
            for (int j = 0; j < BIN_LEN; j++) {
                if (chs[i] == R[j]) {
                    ind = j;
                    break;
                }
            }
            if (chs[i] == B) {
                break;
            }
            if (i > 0) {
                res = res * BIN_LEN + ind;
            } else {
                res = ind;
            }
            // System.err.println(ind + "-->" + res);
        }
        return res;
    }

    /**
     * 生成随即密码
     *
     * @param pwdLen 生成的密码的总长度
     * @return 密码的字符串
     */
    public static String generate(int pwdLen) {
        //35是因为数组是从0开始的，26个字母+10个数字
        final int maxNum = 75;
        //生成的随机数
        int i;
        //生成的密码的长度
        int count = 0;

        char[] str = {
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '~', '!', '@', '#', '$', '%', '^', '&'
        };
        //buffer线程安全，但是性能略低
        StringBuilder pwd = new StringBuilder();
        Random r = new Random();
        while (count < pwdLen) {
            // 生成随机数，取绝对值，防止生成负数，生成的数最大为36-1
            i = Math.abs(r.nextInt(maxNum));
            if (i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    public static void main(String[] args) {
        String serialCode = toSerialCode(15811072705L);
        System.err.println("serialCode = " + serialCode);

        String password = generate(12);
        System.err.println("password = " + password);

    }
}
