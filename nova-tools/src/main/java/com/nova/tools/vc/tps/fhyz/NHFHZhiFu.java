package com.nova.tools.vc.tps.fhyz;

/**
 * @author wangzehui
 * @date 2018/4/24 14:41
 */

public class NHFHZhiFu {
    public static final String memberCinemaLinkId = "8888";
    public static final String phone = "188888888";
    public static final String lockCode = "8888000088888888";
    public static final String tmpSeatPrice = "50";

    public static final String cardNo = "888888";
    public static final String encryptPass = "a7Zrjg4dGVCZqDUSwGvLdmTiJXVFgcdX";
    public static final String sessionCode = "0000000000000003_3788180424L6RT8X_61843208_238D68E6D318CD5E00E0441E2184E249";
    public static final String seatCode = "0000000000000105,0000000000000105";
    public static final String tmpConnectFee = "5.0";
    public static final String totalServiceFee = "5.0";
    public static final String ticketChannelFee = "5.0";


    public static final String tmpPayPrice = "50";

    public static void main(String[] args) {
        String[] array = sessionCode.split("_");
        String[] seats = seatCode.split(",");

        String memberCard = String.format("{\\\"payAmount\\\":\\\"%s\\\",\\\"cardNo\\\":\\\"%s\\\",\\\"token\\\":\\\"%s\\\"}", tmpSeatPrice, cardNo, encryptPass);
        String corp = String.format("{\\\"payAmount\\\":\\\"%s\\\"}", tmpPayPrice);

        String paymentStr = "";

        System.out.println(paymentStr);

        String ticketStr = "";
        for (String seat : seats) {
            ticketStr += String.format(
                    "{\\\"seatCode\\\":\\\"%s\\\",\\\"ticketPrice\\\":\\\"%s\\\",\\\"ticketFee\\\":\\\"%s\\\",\\\"serviceFee\\\":\\\"%s\\\",\\\"ticketChannelFee\\\":\\\"%s\\\"},",
                    seat, tmpSeatPrice, tmpConnectFee, "0.00", "0.00", "N");

            paymentStr += String.format("{\\\"paymentMethodCode\\\":\\\"%s\\\",\\\"MemberCard\\\":%s,\\\"Corp\\\":%s},",
                    "MemberCard", memberCard, corp);
        }
        ticketStr = ticketStr.substring(0, ticketStr.length() - 1);
        paymentStr = paymentStr.substring(0, paymentStr.length() - 1);

//        System.out.println(ticketStr);
        String data = String.format("{\"cinemaLinkId\":\"%s\",\"mobile\":\"%s\",\"lockOrderId\":\"%s\",\"scheduleKey\":\"%s\",\"scheduleId\":\"%s\",\"ticketList\":[\"%s\"],\"paymentList\":[\"%s\"]}",
                memberCinemaLinkId, phone, lockCode, array[2], array[3], ticketStr, paymentStr);
        System.out.println("data=" + data);

    }


}
