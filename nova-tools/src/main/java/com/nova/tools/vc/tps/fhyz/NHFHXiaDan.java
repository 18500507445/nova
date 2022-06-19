package com.nova.tools.vc.tps.fhyz;

/**
 * @author wangzehui
 * @date 2018/4/25 13:46
 */

public class NHFHXiaDan {
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


    public static void main(String[] args) {
//        String[] array = sessionCode.split("_");
//        String[] seats = seatCode.split(",");
//        String ticketStr = "";
//        for (String seat : seats) {
//            ticketStr += "," + String.format(
//                    "{\"seatCode\":\"%s\",\"ticketPrice\":\"%s\",\"ticketFee\":\"%s\",\"serviceFee\":\"%s\",\"ticketChannelFee\":\"%s\",\"benefitFlag\":\"%s\"}",
//                    seat, tmpSeatPrice, totalServiceFee, ticketChannelFee, 0.00, "N");
//        }
//        ticketStr = ticketStr.substring(1);
//
//        String benefitCardNumber = String.format("{\"benefitCardNumber\":\"%s\"}", cardNo);
//
//        String paymentStr = String.format("{\"paymentMethodCode\":\"%s\",\"BenefitCard\":[%s]}", "BenefitCard", benefitCardNumber);
//
//        //paymentStr非必填参数
//        String data = String.format(
//                "{\"cinemaLinkId\":\"%s\",\"lockOrderId\":\"%s\",\"scheduleId\":\"%s\",\"scheduleKey\":\"%s\",\"ticketList\":[%s],\"mobile\":\"%s\",\"paymentList\":[%s]}",
//                memberCinemaLinkId, lockCode, array[2], array[3], ticketStr, phone, paymentStr);
//
//        System.out.println(data);


        String[] array = sessionCode.split("_");
        String[] seats = seatCode.split(",");
        String ticketStr = "";
//        for (String seat : seats) {
//            ticketStr += "," + String.format(
//                    "{\"seatCode\":\"%s\",\"ticketPrice\":\"%s\",\"ticketFee\":\"%s\",\"serviceFee\":\"%s\",\"ticketChannelFee\":\"%s\",\"benefitFlag\":\"%s\"}",
//                    seat, tmpSeatPrice, tmpConnectFee, "0.00", "0.00", "N");
//        }
//        ticketStr = ticketStr.substring(1);
//        String data = String.format(
//                "{\"cinemaLinkId\":\"%s\",\"lockOrderId\":\"%s\",\"scheduleId\":\"%s\",\"scheduleKey\":\"%s\",\"ticketList\":[%s],\"mobile\":\"%s\"}",
//                memberCinemaLinkId, lockCode, array[2], array[3], ticketStr, phone);
//        System.out.println(data);


        for (String seat : seats) {
            ticketStr += String.format(
                    "{\\\"seatCode\\\":\\\"%s\\\",\\\"ticketPrice\\\":\\\"%s\\\",\\\"ticketFee\\\":\\\"%s\\\",\\\"serviceFee\\\":\\\"%s\\\",\\\"ticketChannelFee\\\":\\\"%s\\\"},",
                    seat, tmpSeatPrice, tmpConnectFee, "0.00", "0.00", "N");
        }
        ticketStr = ticketStr.substring(0, ticketStr.length() - 1);
        String data = String.format(
                "{\"cinemaLinkId\":\"%s\",\"lockOrderId\":\"%s\",\"scheduleId\":\"%s\",\"scheduleKey\":\"%s\",\"ticketList\":[\"%s\"],\"mobile\":\"%s\"}",
                memberCinemaLinkId, lockCode, array[2], array[3], ticketStr, phone);
        System.out.println(data);

    }


}
