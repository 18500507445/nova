package com.nova.tools.vc.tps.fhyz;

/**
 * @author wangzehui
 * @date 2018/4/26 10:28
 */

public class HFHZhiFu {
    public static final String cinemaLinkId = "8888";
    public static final String phone = "188888888";
    public static final String lockCode = "8888000088888888";
    public static final String tmpSeatPrice = "50";
    public static final String cardNo = "888888";
    public static final String encryptPass = "a7Zrjg4dGVCZqDUSwGvLdmTiJXVFgcdX";
    public static final String sessionCode = "0000000000000003_3788180424L6RT8X_61843208_238D68E6D318CD5E00E0441E2184E249";
    public static final String seatCode = "0000000000000105,0000000000000104";
    public static final String totalServiceFee = "5.0";
    public static final String ticketChannelFee = "5.0";

    public static void main(String[] args) {
        String[] array = sessionCode.split("_");
        String[] seats = seatCode.split(",");
        String ticketStr = "";

//        String paymentStr = String.format(
//                "{\"paymentMethod\":\"%s\",\"payAmount\":\"%s\",\"payCardInfo\":[{ \"totalDisTickets\": \"%s\", \"cinemaLinkId\": \"%s\", \"cardNumber\":\"%s\" ,\"cardPassword\":\"%s\"},{\"paymentMethod\":\"%s\", \"payAmount\":\"%s\"}]}",
//                "MemberCard", tmpSeatPrice, "1", cinemaLinkId, cardNo, encryptPass, "Corp", ticketChannelFee);
//        for (String seat : seats) {
//            ticketStr += "," + String.format(
//                    "{\"seatId\":\"%s\",\"ticketPrice\":\"%s\",\"ticketFee\":\"%s\",\"paymentList\":[%s]}", seat,
//                    tmpSeatPrice, totalServiceFee, paymentStr);
//        }
//        ticketStr = ticketStr.substring(1);
//        String data = String.format(
//                "{\"cinemaLinkId\":\"%s\",\"mobile\":\"%s\",\"lockOrderId\":\"%s\",\"scheduleId\":\"%s\",\"scheduleKey\":\"%s\",\"ticketList\":[%s]}",
//                cinemaLinkId, phone, lockCode, array[2], array[3], ticketStr);



        String paymentStr = String.format(
                "{\"paymentMethod\":\"%s\",\"payAmount\":\"%s\",\"payCardInfo\":{ \"totalDisTickets\": \"%s\", \"cinemaLinkId\": \"%s\", \"cardNumber\":\"%s\" ,\"cardPassword\":\"%s\"}},{\"paymentMethod\":\"%s\", \"payAmount\":\"%s\"}",
                "MemberCard", tmpSeatPrice,"1", cinemaLinkId,cardNo, encryptPass, "Corp", ticketChannelFee);
        for (String seat : seats) {
            ticketStr += String.format(
                    "{\"seatId\":\"%s\",\"ticketPrice\":\"%s\",\"ticketFee\":\"%s\",\"paymentList\":[%s]},", seat,
                    tmpSeatPrice, totalServiceFee, paymentStr);
        }
        ticketStr = ticketStr.substring(0, ticketStr.length() - 1);
        String data = String.format(
                "{\"cinemaLinkId\":\"%s\",\"mobile\":\"%s\",\"lockOrderId\":\"%s\",\"scheduleId\":\"%s\",\"scheduleKey\":\"%s\",\"ticketList\":[%s]}",
                cinemaLinkId, phone, lockCode, array[2], array[3], ticketStr);

        System.out.println(paymentStr);


        System.out.println(data);




    }
}
