package com.nova.tools.vc.openapi;

/**
 * @author wangzehui
 * @date 2018/10/25 15:58
 */

public class redisKey {

    private static String HFH_offlineCardPrice = "cinemaId:hallId:cardNumber:%s";
    public static void main (String[] args){

        String k = 5842+"_"+"1"+"_"+"2";

        String key = String.format(HFH_offlineCardPrice, k);

        System.out.println(key);

    }


}
