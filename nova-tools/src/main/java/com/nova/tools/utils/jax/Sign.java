package com.nova.tools.utils.jax;

import java.security.MessageDigest;


public class Sign {

    public static String dingXinSign(String signStr, String dingXinAuthCode) {
        String tmpStr = MD5(dingXinAuthCode + signStr);
        return MD5(tmpStr + dingXinAuthCode);
    }

    public static String dingXinSignMember(String signStr, String dingXinAuthCode) {
        String tmpStr = MD5(dingXinAuthCode + signStr);
        return MD5(tmpStr + dingXinAuthCode);
    }

    /**
     * @param password
     * @return
     */
    public static String MD5(String password) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = password.getBytes("UTF-8");
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
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 辰星系统MD5前转换成小写
     *
     * @param signStr
     * @return
     * @throws Exception
     */
    public static String chenXinSign(String signStr) {
        return MD5(signStr.toLowerCase());
    }

    /**
     * 快购云系统MD5后转换成小写
     */
    public static String kuaiGouYunSign(String signStr) {
        return MD5(signStr).toLowerCase();
    }
}
