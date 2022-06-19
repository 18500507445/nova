package com.nova.tools.vc.order.boxoffice;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wangzehui
 * @date 2018/5/13 18:59
 */

public class OrderTime {
    public static Map getParame(String time, int type) throws Exception {
        Map map = new HashMap<String, Object>();
        String[] array = time.split("-");
        if (type == 1) {
            map = getDay(array[0], array[1]);
        } else if (type == 3) {
            map = getWeekDay(array[0], array[1], array[2]);
        } else {
            map.put("time", time);
        }
        return map;
    }

    public static void main(String[] args) {

        Integer newYearMemberCount = 25;
        Integer lastYearMemberCount = 15;

        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);

        Double memberCountPercentage = 0.00;

        memberCountPercentage = (newYearMemberCount.doubleValue() - lastYearMemberCount.doubleValue()) / lastYearMemberCount.doubleValue();

        System.out.println(nt.format(memberCountPercentage));

        Map<String, Object> map = null;
        try {
            map = getParame("2018-10-09", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        String beforeStartTime = (String) map.get("beforeStartTime");
        String beforeEndTime = (String) map.get("beforeEndTime");

        String startWeekDay = (String) map.get("startWeekDay");
        String endWeekDay = (String) map.get("endWeekDay");
        String beforeStartWeekDay = (String) map.get("beforeStartWeekDay");
        String beforeEndWeekDay = (String) map.get("beforeEndWeekDay");

        System.out.println("+++++++++++++++++++++++++处理月++++++++++++++++++++++++");
        System.out.println(startTime);
        System.out.println(endTime);
        System.out.println(beforeStartTime);
        System.out.println(beforeEndTime);

        System.out.println("+++++++++++++++++++++++++处理周++++++++++++++++++++++++");
        System.out.println(startWeekDay);
        System.out.println(endWeekDay);
        System.out.println(beforeStartWeekDay);
        System.out.println(beforeEndWeekDay);


    }


    public static Map getDay(String yrars, String month) throws Exception {
        String dateStr = yrars + "-" + month + "-01";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = format.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();
        //当前月最后一天
        calendar.add(Calendar.MONTH, 1); //加一个月
        calendar.set(Calendar.DATE, 1); //设置为该月第一天
        calendar.add(Calendar.DATE, -1); //再减一天即为上个月最后一天
        String day_last = format.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.add(Calendar.MONTH, -1);
        Date beforeDate = calendar1.getTime();
        gcLast.setTime(beforeDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String beforeStartTime = format.format(gcLast.getTime());
        StringBuffer beforeStr = new StringBuffer().append(beforeStartTime).append(" 00:00:00");
        beforeStartTime = beforeStr.toString();
        //上个月最后一天
        calendar1.add(Calendar.MONTH, 1); //加一个月
        calendar1.set(Calendar.DATE, 1); //设置为该月第一天
        calendar1.add(Calendar.DATE, -1); //再减一天即为上个月最后一天
        String beforeEndTime = format.format(calendar1.getTime());
        StringBuffer beforeStrendStr = new StringBuffer().append(beforeEndTime).append(" 23:59:59");
        beforeEndTime = beforeStrendStr.toString();
        Map<String, String> map = new HashMap<String, String>(16);
        map.put("startTime", day_first);
        map.put("endTime", day_last);
        map.put("beforeStartTime", beforeStartTime);
        map.put("beforeEndTime", beforeEndTime);
        return map;
    }

    public static Map getWeekDay(String years, String month, String day) throws Exception {
        String dateStr = years + "-" + month + "-" + day;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(simpleDateFormat.parse(dateStr));
        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        // 所在周开始日期
        String startWeekDay = simpleDateFormat.format(cal.getTime());
        StringBuffer swd = new StringBuffer().append(startWeekDay).append(" 00:00:00");
        startWeekDay = swd.toString();
        cal.add(Calendar.DAY_OF_WEEK, 6);
        // 所在周结束日期
        String endWeekDay = simpleDateFormat.format(cal.getTime());
        StringBuffer ewd = new StringBuffer().append(endWeekDay).append(" 23:59:59");
        endWeekDay = ewd.toString();
        cal.setTime(simpleDateFormat.parse(startWeekDay));
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        //所在周上一周的开始日期
        String beforeStartWeekDay = simpleDateFormat.format(cal.getTime());
        StringBuffer bsw = new StringBuffer().append(beforeStartWeekDay).append(" 00:00:00");
        beforeStartWeekDay = bsw.toString();
        cal.setTime(simpleDateFormat.parse(endWeekDay));
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        //所在周上一周的结束日期
        String beforeEndWeekDay = simpleDateFormat.format(cal.getTime());
        StringBuffer bew = new StringBuffer().append(beforeEndWeekDay).append(" 23:59:59");
        beforeEndWeekDay = bew.toString();
        Map<String, String> map = new HashMap<String, String>(16);
        map.put("startWeekDay", startWeekDay);
        map.put("endWeekDay", endWeekDay);
        map.put("beforeStartWeekDay", beforeStartWeekDay);
        map.put("beforeEndWeekDay", beforeEndWeekDay);
        return map;
    }


}
