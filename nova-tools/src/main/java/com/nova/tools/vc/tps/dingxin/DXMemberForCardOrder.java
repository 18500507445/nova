package com.nova.tools.vc.tps.dingxin;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author wangzehui
 * @date 2018/10/18 14:10
 */

public class DXMemberForCardOrder {

    public static final String memberPrice = "64.00";

    public static final String seatCode = "1,2,3";

    public static final String cardNo = "";

    public static final String poundage = "0.00";

    public static void main(String[] args) {

        StringBuffer request = new StringBuffer();

        StringBuilder seatCodeParam = new StringBuilder();
        //座位总价格
        BigDecimal sumMemberPrice = new BigDecimal(memberPrice);
        //判断是否为多个座位
        if (seatCode.contains(",")) {
            StringBuilder strBuilder = new StringBuilder();
            String[] seats = seatCode.split(",");
            //座位单价
            BigDecimal tmpMemberPrice = sumMemberPrice.divide(new BigDecimal(seats.length), 2, RoundingMode.HALF_UP);
            int steps = 0;
            if (seats.length > 0) {
                for (int i = 0; i < seats.length; i++) {
                    if (null != seats[i] && !seats[i].isEmpty()) {
                        if (i == seats.length - 1) {
                            //最后一个循环的价格 =  座位单价+（总价-座位单价*座位数量）
                            tmpMemberPrice = tmpMemberPrice.add(sumMemberPrice.subtract(tmpMemberPrice.multiply(new BigDecimal(seats.length)))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                        strBuilder.append(seats[i]).append("-").append(poundage).append("-").append(tmpMemberPrice.toString()).append(StringUtils.isEmpty(cardNo) ? "" : "-1");
                        if (++steps < seats.length) {
                            strBuilder.append(",");
                        }
                    }
                }
            }
            request.append("&seat=").append(strBuilder.toString());
            seatCodeParam.append("&seat=").append(strBuilder.toString());
            System.out.println(request);
            System.out.println(seatCodeParam);
        } else {
            request.append("&seat=").append(seatCode).append("-").append(poundage).append("-").append(memberPrice).append(StringUtils.isEmpty(cardNo) ? "" : "-1");
            seatCodeParam.append("&seat=").append(seatCode).append("-").append(poundage).append("-").append(memberPrice).append(StringUtils.isEmpty(cardNo) ? "" : "-1");
            System.out.println(request);
            System.out.println(seatCodeParam);
        }
    }

}
