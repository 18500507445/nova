package com.nova.tools.utils.hutool.core.date;

import cn.hutool.core.date.*;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * DateTime单元测试
 *
 * @author Looly
 */
public class DateTimeTest {

    @Test
    public void datetimeTest() {
        DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);

        // 年
        int year = dateTime.year();
        Assert.equals(2017, year);

        // 季度（非季节）
        Quarter season = dateTime.quarterEnum();
        Assert.equals(Quarter.Q1, season);

        // 月份
        Month month = dateTime.monthEnum();
        Assert.equals(Month.JANUARY, month);

        // 日
        int day = dateTime.dayOfMonth();
        Assert.equals(5, day);
    }

    @Test
    public void datetimeTest2() {
        DateTime dateTime = new DateTime("2017-01-05 12:34:23");

        // 年
        int year = dateTime.year();
        Assert.equals(2017, year);

        // 季度（非季节）
        Quarter season = dateTime.quarterEnum();
        Assert.equals(Quarter.Q1, season);

        // 月份
        Month month = dateTime.monthEnum();
        Assert.equals(Month.JANUARY, month);

        // 日
        int day = dateTime.dayOfMonth();
        Assert.equals(5, day);
    }

    @Test
    public void quarterTest() {
        DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
        Quarter quarter = dateTime.quarterEnum();
        Assert.equals(Quarter.Q1, quarter);

        dateTime = new DateTime("2017-04-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
        quarter = dateTime.quarterEnum();
        Assert.equals(Quarter.Q2, quarter);

        dateTime = new DateTime("2017-07-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
        quarter = dateTime.quarterEnum();
        Assert.equals(Quarter.Q3, quarter);

        dateTime = new DateTime("2017-10-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
        quarter = dateTime.quarterEnum();
        Assert.equals(Quarter.Q4, quarter);

        // 精确到毫秒
        DateTime beginTime = new DateTime("2017-10-01 00:00:00.000", DatePattern.NORM_DATETIME_MS_FORMAT);
        dateTime = DateUtil.beginOfQuarter(dateTime);
        Assert.equals(beginTime, dateTime);

        // 精确到毫秒
        DateTime endTime = new DateTime("2017-12-31 23:59:59.999", DatePattern.NORM_DATETIME_MS_FORMAT);
        dateTime = DateUtil.endOfQuarter(dateTime);
        Assert.equals(endTime, dateTime);
    }

    @Test
    public void mutableTest() {
        DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);

        // 默认情况下DateTime为可变对象
        DateTime offsite = dateTime.offset(DateField.YEAR, 0);
        Assert.equals(offsite, dateTime);

        // 设置为不可变对象后变动将返回新对象
        dateTime.setMutable(false);
        offsite = dateTime.offset(DateField.YEAR, 0);
        Assert.equals(offsite, dateTime);
    }

    @Test
    public void toStringTest() {
        DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
        Assert.equals("2017-01-05 12:34:23", dateTime.toString());

        String dateStr = dateTime.toString("yyyy/MM/dd");
        Assert.equals("2017/01/05", dateStr);
    }

    @Test
    public void toStringTest2() {
        DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);

        String dateStr = dateTime.toString(DatePattern.UTC_WITH_ZONE_OFFSET_PATTERN);
        Assert.equals("2017-01-05T12:34:23+0800", dateStr);

        dateStr = dateTime.toString(DatePattern.UTC_WITH_XXX_OFFSET_PATTERN);
        Assert.equals("2017-01-05T12:34:23+08:00", dateStr);
    }

    @Test
    public void monthTest() {
        //noinspection ConstantConditions
        int month = DateUtil.parse("2017-07-01").month();
        Assert.equals(6, month);
    }

    @Test
    public void weekOfYearTest() {
        DateTime date = DateUtil.parse("2016-12-27");
        //noinspection ConstantConditions
        Assert.equals(2016, date.year());
        //跨年的周返回的总是1
        Assert.equals(1, date.weekOfYear());
    }

    /**
     * 严格模式下，不允许非常规的数字，如秒部分最多59，99则报错
     */
    @Test
    public void ofTest() {
        String a = "2021-09-27 00:00:99";
        new DateTime(a, DatePattern.NORM_DATETIME_FORMAT, false);
    }
}
