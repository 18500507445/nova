package com.nova.tools.vc.tps.kgycloud;

/**
 * @author wangzehui
 * @date 2018/9/10 14:10
 */

public class KGYComfirmOrder {
    public static final String seatCode = "0000000000000001-1-1,0000000000000001-1-2";

    public static final String seatPrice = "30";

    public static final String ticketPrice = "30";

    public static void main(String[] args) {
        StringBuffer orderSeats = new StringBuffer();
        String[] seats = seatCode.split(",");
        for (String seat : seats) {
            orderSeats.append(seat).append(":").append(seatPrice).append(":").append(ticketPrice).append(",");
        }

//        System.out.println(orderSeats);


        String orderSeat = orderSeats.substring(0, orderSeats.length() - 1);

        System.out.println(orderSeat);

    }
}
