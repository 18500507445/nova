package com.nova.tools.java8.datetime;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Date;

/**
 * 日期转换示例
 *
 * @author biezhi
 * @date 2018/3/3
 */
public class ConvertExample {

    /**
     * LocalDate -> Date
     *
     * @param localDate
     * @return
     */
    @Test
    public void toDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date from = Date.from(instant);
        System.out.println(from);
    }

    /**
     * LocalDateTime -> Date
     *
     * @param localDateTime
     * @return
     */
    @Test
    public void toDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date from = Date.from(instant);
        System.out.println(from);
    }

    /**
     * LocalTime -> Date
     *
     * @param localTime
     * @return
     */
    @Test
    public void toDate(LocalTime localTime) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date from = Date.from(instant);
        System.out.println(from);
    }

    /**
     * Date -> LocalDate
     *
     * @param date
     * @return
     */
    @Test
    public void toLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        System.out.println(localDate);
    }

    /**
     * Date -> LocalDateTime
     *
     * @param date
     * @return
     */
    @Test
    public void toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        System.out.println(localDateTime);
    }

    /**
     * Date -> LocalTime
     *
     * @param date
     * @return
     */
    @Test
    public void toLocalTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalTime localTime = localDateTime.toLocalTime();
        System.out.println(localTime);
    }


}