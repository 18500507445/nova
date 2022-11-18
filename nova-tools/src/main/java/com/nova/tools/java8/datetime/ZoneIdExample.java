package com.nova.tools.java8.datetime;

import org.junit.jupiter.api.Test;

import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/11/18 14:06
 */
public class ZoneIdExample {

    /**
     * ZoneId 示例
     */
    @Test
    public void zoneIdExample() {
        // 获取系统默认时区
        ZoneId defaultZoneId = ZoneId.systemDefault();
        ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");

        // TimeZone 转换为 ZoneId
        ZoneId oldToNewZoneId = TimeZone.getDefault().toZoneId();

        System.out.println(defaultZoneId);
        System.out.println(shanghaiZoneId);
        System.out.println(oldToNewZoneId);

        System.out.println(ZoneId.getAvailableZoneIds());
    }

    /**
     * ZonedDateTime 示例
     */
    @Test
    public void zonedDateTimeExample(){
        // 创建一个ZonedDateTime实例
        ZonedDateTime dateTime = ZonedDateTime.now();

        // 使用指定的年月日、时分秒、纳秒以及时区ID来新建对象
        ZoneId zoneId = ZoneId.of("UTC+8");
        ZonedDateTime dateTime2 = ZonedDateTime.of(2018, 3, 8, 23, 45, 59, 1234, zoneId);

        // 3天后
        ZonedDateTime zoneDateTime = dateTime2.plus(Period.ofDays(3));

        ZoneId zoneId2 = ZoneId.of("Europe/Copenhagen");
        ZoneId zoneId3 = ZoneId.of("Europe/Paris");

        System.out.println("dateTime     : " + dateTime);
        System.out.println("zoneDateTime : " + zoneDateTime);
        System.out.println("zoneId2      : " + zoneId2);
        System.out.println("zoneId3      : " + zoneId3);
    }
}
