package com.nova.common.utils.security;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @Description: 用户名加密工具类
 * @Author: wangzehui
 * @Date: 2022/10/11 17:15
 */
@Slf4j
public class UserNameSecretUtil {

    /**
     * 数字秘钥
     */
    private final static long SECRET_KEY = 16000771L;
    //转换字符（0-9分别为["D","e","C","A","P","b","J","I","z","M"]
    private final static String CONVERT_KEY = "DeCAPbJIzM";
    //混淆字母
    private final static String CONFUSED_WORDS_KEY = "";
    //    private final static String CONFUSED_WORDS_KEY = "FxYNgq";
    //总加密字符串长度
    private final static int LEN_KEY = 32;
    //数字混淆
    private final static Long TIME_KEY = 16000771L;
    //加密内容结尾
    private final static String SECRET_MSG_ENDING = "SECRETMSGENDING";

    public final static List<String> SECRET_KEY_LIST = Arrays.asList("user_name", "USER_NAME", "username", "userName",
            "experts_name", "EXPERTS_NAME", "expertsName", "loginUserName", "attenUserName", "authorUserName",
            "user_name_anchor", "shieldUserName");

    /**
     * 返回内容用户名加密
     *
     * @param str 待加密串
     * @return 结果
     */
    public static String retSecret(String str) {
        String retStr = str;
        //需要加密的内容
        try {
            Set<String> set = new HashSet<>();

            for (String userName : SECRET_KEY_LIST) {
                String jsonStr = str;
                //如果不包含参数，直接返回就可以
                if (retStr.indexOf(userName) < 0) {
                    continue;
                }
                int frontLength = 0;
                while (jsonStr.contains(userName)) {
                    int index = jsonStr.indexOf(userName);
                    frontLength += (index + userName.length());
                    jsonStr = jsonStr.substring(index + userName.length() + 3);
                    String content = jsonStr.substring(0, jsonStr.indexOf("\""));
                    if (content.split("@")[0].length() == 11) {
                        //如果是手机号在保存
                        set.add(content);
                    }
                }
            }
            for (String userName : set) {
                if (userName.indexOf("@") > 0) {
                    String encrypt = encrypt(userName.split("@")[0]) + "@" + userName.split("@")[1] + "_" + SECRET_MSG_ENDING;
                    retStr = retStr.replaceAll(userName, encrypt);
                } else {
                    String encrypt = encrypt(userName) + "_" + SECRET_MSG_ENDING;
                    retStr = retStr.replaceAll(userName, encrypt);
                }
            }
        } catch (Exception e) {
            log.info("retSecret====>retStr:" + retStr);
            e.printStackTrace();
        }
        return retStr;
    }

    public static void main(String[] args) {
        String str = "CPezPzDzJbPMeeIzzJ@dongeqiu_SECRETMSGENDING";
        //String str = "otdolwPL6lOYcwUh7hmEp8IrB6S0@weiXin";
        System.out.println(userNameDecrypt(str));
    }

    /**
     * 单独用户名加密算法
     *
     * @param str 用户名
     * @return
     */
    public static String userNameEncrypt(String str) {
        try {
            String phone = str.split("@")[0];
            if (!isNumber(phone)) {
                return str;
            }
            return encrypt(str.split("@")[0]) + "@" + str.split("@")[1] + "_" + SECRET_MSG_ENDING;
        } catch (Exception ignored) {

        }
        return str;
    }

    /**
     * 用户名解密算法
     *
     * @param userName 用户名
     * @return
     */
    public static String userNameDecrypt(String userName) {
        try {
            if (userName.endsWith(SECRET_MSG_ENDING)) {
                userName = URLDecoder.decode(userName, "utf-8").replace("_" + SECRET_MSG_ENDING, "");
                if (userName.contains("%40")) {
                    userName = URLDecoder.decode(userName, "utf-8");
                }
                userName = decrypt(userName.split("@")[0]) + "@" + userName.split("@")[1];
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return userName;
    }

    public static String encrypt(String str) {
        //数字校验
        if (!isNumber(str)) {
            return str;
        }
        long number = Long.parseLong(str);
        long newNumber = (number + TIME_KEY) * SECRET_KEY;
        String[] numArr = String.valueOf(newNumber).split("");
        String[] initArr = CONVERT_KEY.split("");
        int len = numArr.length;
        StringBuilder buffer = new StringBuilder();
        //数字转字母
        for (String s : numArr) {
            int inx = Integer.parseInt(s);
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
        return buffer.toString();
    }

    /**
     * 解密算法
     */
    public static String decrypt(String str) {
        if (null == str || "".equals(str)) {
            return str;
        }
        int l = CONFUSED_WORDS_KEY.length();
        String[] cwkArr = CONFUSED_WORDS_KEY.split("");
        for (int i = 0; i < l; i++) {
            str = str.replaceAll(cwkArr[i], "");
        }
        String[] initArr = str.split("");
        int len = initArr.length;
        StringBuilder result = new StringBuilder();
        for (String s : initArr) {
            int k = CONVERT_KEY.indexOf(s);
            if (k == -1) {
                return str;
            }
            result.append(k);
        }
        Long number;
        try {
            long total = Long.parseLong(result.toString());
            long sum = total / SECRET_KEY;
            number = sum - TIME_KEY;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return str;
        }
        return number.toString();
    }

    /**
     * 数字校验
     */
    public static boolean isNumber(String value) {
        String pattern = "^[0-9]*[1-9][0-9]*$";
        return Pattern.matches(pattern, value);
    }
}
