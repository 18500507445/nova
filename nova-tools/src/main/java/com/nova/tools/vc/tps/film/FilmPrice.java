package com.nova.tools.vc.tps.film;


import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author wangzehui
 * @date 2018/5/25 16:03
 */

public class FilmPrice {

    public static final String memberPrice = "94.16";

    public static final String seatCode = "105172,105173,105174,105174";

    public static void main(String[] args) {
//       String date = "2019-02-01";
//       try {
//           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//           SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
//           String timeYear = formatYear.format(formatYear.parse(date));
//           String dateYear = format.format(format.parse(timeYear + "-01-01"));
//           System.out.println(dateYear);
//       } catch (ParseException e) {
//           e.printStackTrace();
//       }

//       String cardId = "**0800003";
//       cardId = cardId.replace("*", "");
//       System.out.println(cardId);


//      String s = "\u4f1a\u5458\u5361\u5bc6\u7801\u9519\u8bef";
//
//      System.out.println(s);

        if (seatCode.contains(",")) {
            System.out.println("1");
        }


        //按照，切割seatCode
        String[] seats = seatCode.split(",");
        BigDecimal sumMemberPrice = new BigDecimal(memberPrice);
        //座位单价,29.93
        BigDecimal tmpMemberPrice = sumMemberPrice.divide(new BigDecimal(seats.length), 2, RoundingMode.HALF_UP);
        String memberPriceNew = "";
        //0.01怎么计算出来   memberPrice-tmpMemberPrice*seats.length
        for (int i = 0; i < seats.length; i++) {
            if (i == seats.length - 1) {
                tmpMemberPrice = tmpMemberPrice.add(sumMemberPrice.subtract(tmpMemberPrice.multiply(new BigDecimal(seats.length)))).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            memberPriceNew += "," + tmpMemberPrice.toString();

        }
        memberPriceNew = memberPriceNew.substring(1, memberPriceNew.length());

        System.out.println(memberPriceNew);
    }


}