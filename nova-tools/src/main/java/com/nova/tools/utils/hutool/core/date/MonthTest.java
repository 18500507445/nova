package com.nova.tools.utils.hutool.core.date;

import cn.hutool.core.date.Month;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

public class MonthTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    public void getLastDayTest() {
        int lastDay = Month.of(Calendar.JANUARY).getLastDay(false);
        Assert.equals(31, lastDay);
        lastDay = Month.of(Calendar.FEBRUARY).getLastDay(false);
        Assert.equals(28, lastDay);
        lastDay = Month.of(Calendar.FEBRUARY).getLastDay(true);
        Assert.equals(29, lastDay);
        lastDay = Month.of(Calendar.MARCH).getLastDay(true);
        Assert.equals(31, lastDay);
        lastDay = Month.of(Calendar.APRIL).getLastDay(true);
        Assert.equals(30, lastDay);
        lastDay = Month.of(Calendar.MAY).getLastDay(true);
        Assert.equals(31, lastDay);
        lastDay = Month.of(Calendar.JUNE).getLastDay(true);
        Assert.equals(30, lastDay);
        lastDay = Month.of(Calendar.JULY).getLastDay(true);
        Assert.equals(31, lastDay);
        lastDay = Month.of(Calendar.AUGUST).getLastDay(true);
        Assert.equals(31, lastDay);
        lastDay = Month.of(Calendar.SEPTEMBER).getLastDay(true);
        Assert.equals(30, lastDay);
        lastDay = Month.of(Calendar.OCTOBER).getLastDay(true);
        Assert.equals(31, lastDay);
        lastDay = Month.of(Calendar.NOVEMBER).getLastDay(true);
        Assert.equals(30, lastDay);
        lastDay = Month.of(Calendar.DECEMBER).getLastDay(true);
        Assert.equals(31, lastDay);
    }

    @Test
    public void toJdkMonthTest() {
        final java.time.Month month = Month.AUGUST.toJdkMonth();
        Assert.equals(java.time.Month.AUGUST, month);
    }

    @Test
    public void toJdkMonthTest2() {
        Month.UNDECIMBER.toJdkMonth();
    }

    @Test
    public void ofTest() {
        Month month = Month.of("Jan");
        Assert.equals(Month.JANUARY, month);

        month = Month.of("JAN");
        Assert.equals(Month.JANUARY, month);

        month = Month.of("FEBRUARY");
        Assert.equals(Month.FEBRUARY, month);

        month = Month.of("February");
        Assert.equals(Month.FEBRUARY, month);

        month = Month.of(java.time.Month.FEBRUARY);
        Assert.equals(Month.FEBRUARY, month);
    }
}
