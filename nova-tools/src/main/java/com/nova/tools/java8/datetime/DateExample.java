package com.nova.tools.java8.datetime;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @description: DateExample
 * @author: wzh
 * @date: 2022/11/18 14:06
 */
class DateExample {

    /**
     * DateTimeFormatter 示例
     */
    @Test
    void dateTimeFormatExample() {
        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        String formattedDate = formatter.format(LocalDate.now());
        String formattedZonedDate = formatter.format(ZonedDateTime.now());

        System.err.println("LocalDate          : " + formattedDate);
        System.err.println("formattedZonedDate : " + formattedZonedDate);

        LocalDateTime dateTime = LocalDateTime.now();
        // 20180303
        String strDate1 = dateTime.format(DateTimeFormatter.BASIC_ISO_DATE);
        // 2013-03-03
        String strDate2 = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
        // 当前时间
        String strDate3 = dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
        // 2018-03-03
        String strDate4 = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 今天是：2018年 三月 03日 星期六
        String strDate5 = dateTime.format(DateTimeFormatter.ofPattern("今天是：YYYY年 MMMM dd日 E", Locale.CHINESE));

        System.err.println(strDate1);
        System.err.println(strDate2);
        System.err.println(strDate3);
        System.err.println(strDate4);
        System.err.println(strDate5);

        // 将一个字符串解析成一个日期对象
        String strDate6 = "2018-03-03";
        String strDate7 = "2017-03-03 15:30:05";

        LocalDate date = LocalDate.parse(strDate6, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime dateTime1 = LocalDateTime.parse(strDate7, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.err.println(date);
        System.err.println(dateTime1);
    }

    /**
     * LocalDate 示例
     */
    @Test
    void localDateExample() {
        // 创建一个LocalDate实例
        LocalDate localDate = LocalDate.now();

        // 使用年月日信息构造出LocalDate对象
        LocalDate localDate2 = LocalDate.of(2018, 3, 3);

        int year = localDate.getYear();
        Month month = localDate.getMonth();
        int dayOfMonth = localDate.getDayOfMonth();
        int dayOfYear = localDate.getDayOfYear();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        System.err.println("year       : " + year);
        System.err.println("month      : " + month.getValue());
        System.err.println("dayOfMonth : " + dayOfMonth);
        System.err.println("dayOfYear  : " + dayOfYear);
        System.err.println("dayOfWeek  : " + dayOfWeek.getValue());

        // 3年后
        LocalDate d1 = localDate2.plusYears(3);
        // 3年前
        LocalDate d2 = localDate2.minusYears(3);
        System.err.println("plusYears  : " + d1);
        System.err.println("minusYears : " + d2);
    }

    /**
     * LocalDateTime 示例
     */
    @Test
    void localDateTimeExample() {
        // 创建一个LocalDateTime实例
        LocalDateTime localDateTime = LocalDateTime.now();

        // 使用指定的年月日、时分秒、纳秒来新建对象
        LocalDateTime localDateTime2 = LocalDateTime.of(2018, 11, 26, 13, 55, 36, 123);

        // 3年后的现在
        LocalDateTime dt1 = localDateTime.plusYears(3);
        // 3年前的现在
        LocalDateTime dt2 = localDateTime.minusYears(3);

        System.err.println("localDateTime  : " + localDateTime);
        System.err.println("localDateTime2 : " + localDateTime2);
        System.err.println("dt1            : " + dt1);
        System.err.println("dt2            : " + dt2);
    }

    /**
     * LocalTime 示例
     */
    @Test
    void localTimeExample() {

        // 创建一个LocalTime实例
        LocalTime localTime = LocalTime.now();

        // 使用指定的时分秒和纳秒来新建对象
        LocalTime localTime2 = LocalTime.of(21, 30, 59, 11001);

        // 3小时后
        LocalTime localTimeLater = localTime.plusHours(3);
        // 3小时前
        LocalTime localTimeEarlier = localTime.minusHours(3);

        System.err.println("localTime       : " + localTime);
        System.err.println("localTime2      : " + localTime2);
        System.err.println("localTimeLater  : " + localTimeLater);
        System.err.println("localTimeEarlier: " + localTimeEarlier);
    }

}
