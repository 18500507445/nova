package com.nova.tools.vc.tps.fhyz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wangzehui
 * @date 2018/5/16 19:18
 */

public class HFHDiscount {

    public static void main(String[] args) {

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("demo/price", "20.00");
        map1.put("isCardDiscount", "false");
        map1.put("ticketType", "VC会员");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("demo/price", "80.00");
        map2.put("isCardDiscount", "false");
        map2.put("ticketType", "标准票");
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("demo/price", "30.00");
        map3.put("isCardDiscount", "false");
        map3.put("ticketType", "VC会员_会员价");
        list.add(map1);
        list.add(map2);
        list.add(map3);

        String price = "";
        String maxPrice = "0.0";

        for (Map<String, String> map : list) {
            if ("true".equals(map.get("isCardDiscount"))) {
                if (map.get("ticketType").contains("vc") || map.get("ticketType").contains("vc")) {
                    if (Double.parseDouble(map.get("demo/price")) > Double.parseDouble(price)) {
                        price = map.get("demo/price");
                    }
                }
            }
            if (Double.parseDouble(map.get("demo/price")) > Double.parseDouble(maxPrice)) {
                maxPrice = map.get("demo/price");
            }
        }


        if (("").equals(price) || price == null) {
            System.out.println(maxPrice);
        } else {
            System.out.println(price);
        }


    }


}
