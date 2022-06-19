package com.nova.tools.vc.tps.hln;


import com.nova.tools.utils.jax.Sign;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2018/11/27 13:52
 */

public class TestSign {

    public static void main(String[] args) {

        StringBuilder sb1 = new StringBuilder();
        sb1.append("lockSeats.xml?");
        sb1.append("channelCode=00001");
        sb1.append("&channelShowCode=13027101181041332582");
        sb1.append("&seatCodes=00000501_%23%233_%23%239");
        sb1.append("&channelOrderCode=12018112702445670");
        System.out.println(sendRequestGetBean(sb1.toString()));

        StringBuilder sb2 = new StringBuilder();
        sb2.append("lockSeats.xml?");
        sb2.append("channelCode=00001");
        sb2.append("&channelShowCode=13027101181041332582");
        sb2.append("&seatCodes=00000501_##3_##9");
        sb2.append("&channelOrderCode=12018112702445670");
        System.out.println(sendRequestGetBean(sb2.toString()));


        StringBuilder sb3 = new StringBuilder();
        sb3.append("lockSeats.xml?");
        sb3.append("channelCode=00001");
        sb3.append("&channelShowCode=13027101181041547210");
        sb3.append("&seatCodes=00000101_%23%232_%2313");
        sb3.append("&channelOrderCode=62511415615200381");
        System.out.println(sendRequestGetBean(sb3.toString()));

        StringBuilder sb4 = new StringBuilder();
        sb4.append("releaseSeats.xml?");
        sb4.append("channelCode=00001");
        sb4.append("&orderCode=1067784506573983744");
        System.out.println(sendRequestGetBean(sb4.toString()));


        StringBuilder sb5 = new StringBuilder();
        sb5.append("submitOrder.xml?");
        sb5.append("channelCode=00001");
        sb5.append("&orderCode=1069472058192318464");
        sb5.append("&mobile=18500507445");
        sb5.append("&orderSeats=00000101_%23%239_%23%231%3A25.00%3A25.00");
        System.out.println(sendRequestGetBean(sb5.toString()));

        StringBuilder sb6 = new StringBuilder();
        sb6.append("submitOrder.xml?");
        sb6.append("channelCode=00001");
        sb6.append("&orderCode=1068330753345064960");
        sb6.append("&mobile=15033908981");
        sb6.append("&orderSeats=00000201_##9_##1:25.00:25.00,00000201_##9_##2:25.00:25.00");
        System.out.println(sendRequestGetBean(sb6.toString()));



    }


    /**
     * 发送请求返回对象
     *
     * @param requestHttp
     * @return
     */
    private static String sendRequestGetBean(String requestHttp) {
        String param = requestHttp.substring(requestHttp.indexOf("?") + 1);
        String name = requestHttp.substring(0, requestHttp.indexOf("?") + 1);
        String host = "http://api.loongcinema.com:6620/api/ticket/v1/";

        requestHttp = host + name + param + "&sign=" + getTargetSign(param);

        System.out.println("加密的参数：" + param);
        System.out.println("sign :" + getTargetSign(param));
        return requestHttp;

    }

    /**
     * 签名生成
     *
     * @return
     * @params param
     */
    public static String getTargetSign(String param) {
        Map<String, Object> paramsMap = new TreeMap<String, Object>();
        String[] params = param.split("&");

        // 参数按key进行ASCII码升序排列
        for (int i = 0; i < params.length; i++) {
            String[] values = params[i].split("=");
            paramsMap.put(values[0], values[1]);
        }

        // 将排序后的参数用&拼接起来
        List<String> paramPairs = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            paramPairs.add(entry.getKey() + "=" + entry.getValue());
        }
        String paramSignStr = StringUtils.join(paramPairs, "&");

        // 按UTF-8进行URL编码
        try {
            paramSignStr = URLDecoder.decode(paramSignStr, "UTF-8");

            paramSignStr = URLEncoder.encode(paramSignStr, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 通讯密钥加在编码后的字符串的前面和后面，把得到的字符串进行MD5（RFC 1321标准），得到sign。
        String secKey = "tIEnvofqyKs31SFt";
        String targetSign = Sign.MD5(secKey + paramSignStr + secKey);
        return targetSign;
    }
}
