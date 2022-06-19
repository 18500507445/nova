package com.nova.tools.vc.order.boxoffice;

import java.text.NumberFormat;

public class TongBi {
    public static final String memberCinemaLinkId = "8888";
    public static final String phone = "188888888";
    public static final String lockCode = "8888000088888888";
    public static final String tmpSeatPrice = "50";
    public static final String cardNo = "888888";
    public static final String encryptPass = "a7Zrjg4dGVCZqDUSwGvLdmTiJXVFgcdX";
    public static final String sessionCode = "0000000000000003_3788180424L6RT8X_61843208_238D68E6D318CD5E00E0441E2184E249";
    public static final String seatCode = "0000000000000105,0000000000000104";
    public static final String tmpConnectFee = "5.0";
    public static final String totalServiceFee = "5.0";
    public static final String ticketChannelFee = "5.0";

    public static final String seatPrice = "50.00";
    public static final String payPrice = "55.00";
    public static final String count = "2";
    public static final String poundage = "2.0";
    public static final String connectFee = "3.0";

    public static void main(String[] args) {
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);

        Integer newYearMemberCount = 732323232;

        Integer lastYearMemberCount = 0;

        Double memberCountPercentage;
        //新增会员同比


        if (lastYearMemberCount == 0) {
            System.out.println("分母为0，值为-");
        } else if (newYearMemberCount.equals(lastYearMemberCount)) {
            System.out.println("分子为0，值为0.00%");
        } else {
            memberCountPercentage = (newYearMemberCount.doubleValue() - lastYearMemberCount.doubleValue()) / lastYearMemberCount.doubleValue();
            System.out.println(nt.format(memberCountPercentage));
        }

    }

}
