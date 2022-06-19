package com.nova.tools.vc.tps.hln;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2018/11/27 14:24
 */

public class UrlEncodeTest {
    public static void main (String[] args){
       String s = "0300000400901001";


        try {
            String encode = URLEncoder.encode(s, "UTF-8");


            System.out.println(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }





    }
}
