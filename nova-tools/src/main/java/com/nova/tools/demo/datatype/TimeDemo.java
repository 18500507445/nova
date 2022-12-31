package com.nova.tools.demo.datatype;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @description:
 * @author: wzh
 * @date: 2018/12/20 18:29
 */

public class TimeDemo {

    @Test
    public void localDateDemo() {
        //LocalDate类型对应Mysql数据库Date类型
        LocalDate now = LocalDate.now();
        System.out.println("LocalDate类型对应Mysql数据库Date类型:" + now);


        //LocalDateTime类型对应Mysql数据库DateTime据类型
        LocalDateTime now1 = LocalDateTime.now();
        System.out.println("LocalDateTime类型对应Mysql数据库DateTime据类型:" + now1);

        //LocalTime类型对应Mysql数据库Time据类型
        LocalTime now2 = LocalTime.now();
        System.out.println("LocalTime类型对应Mysql数据库Time据类型:" + now2);

        //LocalDateTime日期类型解析成字符串
        LocalDateTime now3 = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr1 = now3.format(format);
        System.out.println("LocalDateTime日期类型解析成字符串:" + nowStr1);

        //字符串日期解析成LocalDateTime类型，对应Mysql数据库DateTime类型
        String str1 = "2018-12-20 18:40:53";
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse1 = LocalDateTime.parse(str1, format1);
        System.out.println("字符串日期解析成LocalDateTime类型，对应Mysql数据库DateTime类型:" + parse1);
    }


}
