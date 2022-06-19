package com.nova.tools.demo.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeTest {

    public static void main(String[] args) {
        getTimeWay1();
        getTimeWay2();
        getTimeWay3();
        getTimeWay4();
        //获取当前时间精确到毫秒
        getTimeWay5();
    }

    public static void getTimeWay1() {
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(day));
    }


    public static void getTimeWay2() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy：MM：dd HH:mm:ss");
        System.out.println(df.format(System.currentTimeMillis()));

    }

    public static void getTimeWay3() {
        //可以对每个时间域单独修改
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        System.out.println(year + "/" + month + "/" + date + " " + hour + ":" + minute + ":" + second);
    }

    public static void getTimeWay4() {
        Date date = new Date();
        String year = String.format("%tY", date);
        String month = String.format("%tB", date);
        String day = String.format("%te", date);
        System.out.println("现在时间：" + year + "/" + month + "/" + day);
    }


    public static void getTimeWay5() {
        Long now = System.currentTimeMillis();
        System.out.println(now);

    }
}
