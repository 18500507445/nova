package com.nova.tools.vc.global.hfh;

import java.math.BigDecimal;

/**
 * @author wangzehui
 * @date 2018/10/3 9:58
 */

public class HFHCardProcess {

    public static final BigDecimal serviceFeeTps = new BigDecimal(0.00);

    public static final BigDecimal serviceFeePlatform = new BigDecimal(3.00);

    public static final BigDecimal serviceFeeCinema = new BigDecimal(4.00);

    public static final BigDecimal count = new BigDecimal(1.00);

    public static void main (String[] args){
        String serviceFee;
        //如果有系统商服务费时
        if(BigDecimal.ZERO.compareTo(serviceFeeTps)<0){
            serviceFee = (serviceFeeTps.add(serviceFeePlatform).add(serviceFeeCinema)).divide(count) + "";
        }else{
            serviceFee = (serviceFeePlatform.add(serviceFeeCinema)).divide(count) + "";
        }

        System.out.println(serviceFee);
    }
}
