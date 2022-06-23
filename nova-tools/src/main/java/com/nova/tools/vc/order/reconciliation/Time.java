package com.nova.tools.vc.order.reconciliation;



import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/3/19 17:46
 */

public class Time {

    public static void main(String[] args) {
        String timeStr = "1-2-2-2-2-1";

        String[] split = timeStr.split("-");

        if (split.length >= 2 && StringUtils.isNotBlank(split[0]) && StringUtils.isNotBlank(split[1])) {
            String firstDayOfMonth = getFirstDayOfMonth(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            String lastDayOfMonth = getLastDayOfMonth(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            System.out.println(firstDayOfMonth);
            System.out.println(lastDayOfMonth);
        } else {
            System.out.println("数组为空");
        }
    }

    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime()) + " 00:00:00";
        return firstDayOfMonth;
    }

    /**
     * 获得该月最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime()) + " 23:59:59";
        return lastDayOfMonth;
    }


}
