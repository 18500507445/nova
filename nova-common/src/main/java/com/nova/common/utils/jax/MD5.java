package com.nova.common.utils.jax;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.Collator;
import java.util.*;

public class MD5 {
    public static void main(String[] args) {
//
//        String sbkey = "VCMEMBER62549174{\"cardLevelCode\":\"ALL\", \"planInfos\":[{\"featureAppNo\":\"2423201808281006\",\"startTime\":\"2018-08-28 12:25:00\",\"filmCode\":\"51901022018\",\"hallType\":\"3D 巨幕影厅\",\"listingPrice\": 80.00,\"lowestPrice\": 30.00}]}0e10adc3949ba59abbe56e057f20f883e";
//        String s = chenXinSign(sbkey);
//        System.out.println(s);


        String param = "channelCode=vc2018&password=123456";
        String s = getTargetSign(param);
        System.out.println(s);


//        String s = MD5("gzxxx%265130139261%2618500507445%26943236775%2604010112%3A40%3A10%26223b52647dc24b389a6b723c61e66656");
//        System.out.println("+++++++++小写"+s.toLowerCase());
//        String s1 = chenXinSign("gzxxx%265130139261%2618500507445%26943236775%2604010112%3A40%3A10%26223b52647dc24b389a6b723c61e66656");
//        System.out.println(s);
//        System.out.println(s1);

    }


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
        String paramSignStr = StringUtils.join(paramPairs, "&");


        // 按UTF-8进行URL编码
        try {
            paramSignStr = URLEncoder.encode(paramSignStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //把得到的字符串进行 MD5（转小写），得到 sign
        String targetSign = chenXinSign(paramSignStr);
        return targetSign;
    }


    public static String hfhSign(String param) {
        List<String> list = new ArrayList<>();
        String[] params = param.split("&");

        System.out.println("*********2" + params.toString());

        // 参数按key进行ASCII码升序排列
        for (String s : params) {
            String[] values = s.split("=");
            list.add(values[0] + values[1]);
        }
        Collections.sort(list, Collator.getInstance(Locale.ENGLISH));

        String paramSignStr = StringUtils.join(list, "");

        System.out.println("参数签名内容:[{" + paramSignStr + "}]");

        return null;
    }


    /**
     * @param password
     * @return
     */
    public static String MD5(String password) {
        char [] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
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

}
