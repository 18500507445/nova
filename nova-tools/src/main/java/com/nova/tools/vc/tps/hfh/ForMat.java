package com.nova.tools.vc.tps.hfh;

/**
 * @author wangzehui
 */
public class ForMat {
    public static void main (String[] args){
        stringForMat("18912018022411948","18912018022411948");

        String data = String.format("{\"cinemaLinkId\":\"%s\",\"cardNumber\":\"%s\",\"outTradeNo\":\"%s\",\"rechargeAmount\":\"%s\",\"description\":\"%s\"}", "3616", "002730", "32018100402797928", "100.00", null);

        System.out.println(data);
    }


    public static void stringForMat(String cardOrderId,String outTradeNo){
        String data = String.format("{\"cardOrderIdStr\":\"%s\",\"outTradeNo\":\"%s\"}",cardOrderId,outTradeNo);
        System.out.println(data);
    }



}
