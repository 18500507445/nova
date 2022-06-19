package com.nova.tools.vc.tps.kgycloud;

/**
 * @author wangzehui
 * @date 2018/9/5 11:38
 */

public class KGYLockSeat {


    public static final String Token = "015ae41583164f459f29f796a6ff5565";
    public static final String CinemaId = "888";
    public static final String FeatureAppNo = "66846103";
    public static final String SerialNum = "2017081613361012354";
    public static final String SeatNo = "0000000000000001-1-1,0000000000000001-1-2";
    public static final double TicketPrice = 38.00;
    public static final double Handlingfee = 1.00;
    public static final String RecvMobilePhone = "xxxxxx";
    public static final String Sign = "a2fc4544100baf3574e1499ed3addd40";


    public static void main(String[] args) {

        String[] seats = SeatNo.split(",");

        String seatInfoStr = "";

        for (String seat : seats) {
            seatInfoStr += String.format("{\"SeatNo\":\"%s\",\"TicketPrice\":%s},", seat, TicketPrice);
        }
        seatInfoStr = seatInfoStr.substring(0, seatInfoStr.length() - 1);

        String jsonParam = String.format("{\"Token\":\"%s\",\"CinemaId\":\"%s\",\"FeatureAppNo\":\"%s\",\"SerialNum\":\"%s\",\"SeatInfos\":[%s],\"RecvMobilePhone\":\"%s\",\"Sign\":\"%s\"}",
                Token, CinemaId, FeatureAppNo, SerialNum, seatInfoStr, RecvMobilePhone, Sign);


        System.out.println(jsonParam);


    }


}
