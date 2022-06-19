package com.nova.tools.vc.tps.chenxing;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author wangzehui
 * @date 2018/9/25 15:34
 */

public class Time {

  public static void main (String[] args) throws ParseException {
      String firstAndLastOfWeek = getFirstAndLastOfWeek("2018-10-09", "yyyy-MM-dd", "yyyy-MM-dd");
      System.out.println(firstAndLastOfWeek);
  }


    /**
     * 每周的第一天和最后一天
     * @param dataStr
     * @param dateFormat
     * @param resultDateFormat
     * @return
     * @throws ParseException
     */
    public static String getFirstAndLastOfWeek(String dataStr,String dateFormat,String resultDateFormat) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new SimpleDateFormat(dateFormat).parse(dataStr));
        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        // 所在周开始日期
        String data1 = new SimpleDateFormat(resultDateFormat).format(cal.getTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        // 所在周结束日期
        String data2 = new SimpleDateFormat(resultDateFormat).format(cal.getTime());
        cal.setTime(new SimpleDateFormat(dateFormat).parse(data1));
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        String data3 = new SimpleDateFormat(resultDateFormat).format(cal.getTime());
        cal.setTime(new SimpleDateFormat(dateFormat).parse(data2));
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        String data4 = new SimpleDateFormat(resultDateFormat).format(cal.getTime());
        return data1 + "_" + data2+"上一周的年月日：：：："+data3+"_"+data4;

    }


}
