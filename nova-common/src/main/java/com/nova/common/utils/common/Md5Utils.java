package com.nova.common.utils.common;

import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.Collator;
import java.util.*;

/**
 * Md5加密方法
 * @author wzh
 */
@Slf4j(topic = "Md5Utils")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Md5Utils {

    public static String getTargetSign(String param) {
        Map<String, Object> paramsMap = new TreeMap<String, Object>();
        String[] params = param.split("&");

        // 参数按key进行ASCII码升序排列
        for (String s : params) {
            String[] values = s.split("=");
            paramsMap.put(values[0], values[1]);
        }

        // 将排序后的参数用&拼接起来
        List<String> paramPairs = new ArrayList<>();
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            paramPairs.add(String.valueOf(entry.getValue()));
        }
        String paramSignStr = StrUtil.join("&", paramPairs);

        // 按UTF-8进行URL编码
        try {
            paramSignStr = URLEncoder.encode(paramSignStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("异常信息:", e);
        }

        //把得到的字符串进行 MD5（转小写），得到 sign
        return chenXinSign(paramSignStr);
    }


    public static String hfhSign(String param) {
        List<String> list = new ArrayList<>();
        String[] params = param.split("&");

        System.err.println("*********2" + Arrays.toString(params));

        // 参数按key进行ASCII码升序排列
        for (String s : params) {
            String[] values = s.split("=");
            list.add(values[0] + values[1]);
        }
        list.sort(Collator.getInstance(Locale.ENGLISH));

        String paramSignStr = StrUtil.join("", list);

        System.err.println("参数签名内容:[{" + paramSignStr + "}]");

        return null;
    }


    /**
     * @param password
     */
    public static String MD5(String password) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = password.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error("异常信息:", e);
            return "";
        }
    }

    /**
     * 辰星系统MD5前转换成小写
     *
     * @param signStr
     * @return
     */
    public static String chenXinSign(String signStr) {
        return MD5(signStr.toLowerCase());
    }

    private static byte[] md5(String s) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(s.getBytes(StandardCharsets.UTF_8));
            return algorithm.digest();
        } catch (Exception e) {
            log.error("MD5 Error...", e);
        }
        return null;
    }

    private static String toHex(byte hash[]) {
        if (hash == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String hash(String s) {
        try {
            return new String(Objects.requireNonNull(toHex(md5(s))).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("not supported charset...", e);
            return s;
        }
    }
}