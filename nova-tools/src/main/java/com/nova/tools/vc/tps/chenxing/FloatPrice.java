package com.nova.tools.vc.tps.chenxing;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/2/26 11:42
 */

public class FloatPrice {
    public static void main(String[] args) {

        //票价
        String price = "75.00";

        //标准价
        String standPrice = "48";

        //上浮值
        String floatPrice = "0.33";


//        //固定，票价> 标准价+上浮值
//        if (new BigDecimal(price).compareTo(new BigDecimal(standPrice).add(new BigDecimal(floatPrice))) > 0) {
//            price = (new BigDecimal(standPrice).add(new BigDecimal(floatPrice))).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString();
//            System.out.println(price);
//        } else {
//            System.out.println(price);
//        }


        //百分比   票价> 标准价+标准价*上浮值
        if (new BigDecimal(price).compareTo(new BigDecimal(standPrice).add(new BigDecimal(standPrice).multiply(new BigDecimal(floatPrice).divide(new BigDecimal(100))))) > 0) {
            price = (new BigDecimal(standPrice).add(new BigDecimal(standPrice).multiply(new BigDecimal(floatPrice).divide(new BigDecimal(100))))).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
            System.out.println(price);
        } else {
            System.out.println(price);
        }

    }
}
