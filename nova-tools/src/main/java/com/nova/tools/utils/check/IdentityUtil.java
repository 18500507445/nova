package com.nova.tools.utils.check;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 易宝支付工具类
 * @Author: wangzehui
 * @Date: 2022/7/4 15:57
 */
public class IdentityUtil {

    /**
     * 解析地址
     *
     * @param address
     * @return
     */
    public static Map<String, String> getAddressInfo(String address) {
        //1级 省 自治区  2级 市 自治州 地区 3级：区县市旗(镇？)
        String province = null, city = null, provinceAndCity, area = null;
        Map<String, String> row = new LinkedHashMap<>();
        List<Map<String, String>> table = new ArrayList<>();
        Map<String, String> resultMap = new LinkedHashMap<>(4);
        if (address.startsWith("香港特别行政区")) {
            resultMap.put("province", "香港");
            return resultMap;
        } else if (address.contains("澳门特别行政区")) {
            resultMap.put("province", "澳门");
            return resultMap;
        } else if (address.contains("台湾")) {
            resultMap.put("province", "台湾");
            return resultMap;
        } else {
            //普通地址
            String regex = "((?<provinceAndCity>[^市]+市|.*?自治州|.*?区|.*县)(?<town>[^区]+区|.*?市|.*?县|.*?路|.*?街|.*?道|.*?镇|.*?旗)(?<detailAddress>.*))";
            Matcher m = Pattern.compile(regex).matcher(address);
            while (m.find()) {
                provinceAndCity = m.group("provinceAndCity");
                String regex2 = "((?<province>[^省]+省|.+自治区|上海市|北京市|天津市|重庆市|上海|北京|天津|重庆)(?<city>.*))";
                Matcher m2 = Pattern.compile(regex2).matcher(provinceAndCity);
                while (m2.find()) {
                    province = m2.group("province");
                    row.put("province", province == null ? "" : province.trim());
                    city = m2.group("city");
                    row.put("city", city == null ? "" : city.trim());
                }
                area = m.group("town");
                row.put("town", area == null ? "" : area.trim());
                table.add(row);
            }
        }
        if (table != null && table.size() > 0) {
            if (StringUtils.isNotBlank(table.get(0).get("province"))) {
                province = table.get(0).get("province");
                //对自治区进行处理
                if (province.contains("自治区")) {
                    if (province.contains("内蒙古")) {
                        province = province.substring(0, 4);
                    } else {
                        province = province.substring(0, 3);
                    }

                }
            }
            if (StringUtils.isNotBlank(province)) {
                if (StringUtils.isNotBlank(table.get(0).get("city"))) {
                    city = table.get(0).get("city");
                    if (city.equals("上海市") || city.equals("重庆市") || city.equals("北京市") || city.equals("天津市")) {
                        province = table.get(0).get("city");
                    }
                } else if (province.equals("上海市") || province.equals("重庆市") || province.equals("北京市") || province.equals("天津市")) {
                    city = province;
                }
                if (StringUtils.isNotBlank(table.get(0).get("town"))) {
                    area = table.get(0).get("town");
                }
                province = province.substring(0, province.length() - 1);

            }

        } else {
            if (address.contains("省")) {
                String[] provinceSplit = address.split("省");
                if (provinceSplit.length > 0 && StringUtils.isNotBlank(provinceSplit[0])) {
                    resultMap.put("province", provinceSplit[0]);
                }
            }
            if (address.contains("市")) {
                String[] citySplit = address.split("市");
                if (citySplit.length > 0 && StringUtils.isNotBlank(citySplit[0])) {
                    resultMap.put("city", citySplit[0]);
                }
            }
            return resultMap;
        }
        if (StringUtils.isBlank(province)) {
            if (address.contains("省")) {
                String[] provinceSplit = address.split("省");
                if (provinceSplit.length > 0 && StringUtils.isNotBlank(provinceSplit[0])) {
                    resultMap.put("province", provinceSplit[0]);
                }
            }
        } else {
            resultMap.put("province", province);
        }
        if (StringUtils.isBlank(city)) {
            if (address.contains("市")) {
                String[] citySplit = address.split("市");
                if (citySplit.length > 0 && StringUtils.isNotBlank(citySplit[0])) {
                    resultMap.put("city", citySplit[0]);
                }
            }
        } else {
            resultMap.put("city", city);
        }
        resultMap.put("area", area);
        return resultMap;
    }


    /**
     * 校验银行卡号方法
     *
     * @param bankCard
     * @return
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    public static void main(String[] args) {
        String str1 = "河南省杞县五里河乡曹岗村379号";
        String str2 = "河南省南川县冷水镇冷水村马路大组";
        String str3 = "北京市平谷区东高村镇南小街30号";
        Map<String, String> map = getAddressInfo(str2);
        map.entrySet().stream().forEach(item -> System.out.println(item.getKey() + ":" + item.getValue()));

        String cardNo = "6226125518422465852";
        try {
            boolean b = checkBankCard(cardNo);
            System.out.println(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
