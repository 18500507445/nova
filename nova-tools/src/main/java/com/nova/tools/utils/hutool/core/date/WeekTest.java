package com.nova.tools.utils.hutool.core.date;

import cn.hutool.core.date.Week;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

public class WeekTest {

    @Test
    public void ofTest() {
        //测试别名及大小写
        Assert.equals(Week.SUNDAY, Week.of("sun"));
        Assert.equals(Week.SUNDAY, Week.of("SUN"));
        Assert.equals(Week.SUNDAY, Week.of("Sun"));
        //测试全名及大小写
        Assert.equals(Week.SUNDAY, Week.of("sunday"));
        Assert.equals(Week.SUNDAY, Week.of("Sunday"));
        Assert.equals(Week.SUNDAY, Week.of("SUNDAY"));

        Assert.equals(Week.MONDAY, Week.of("Mon"));
        Assert.equals(Week.MONDAY, Week.of("Monday"));

        Assert.equals(Week.TUESDAY, Week.of("tue"));
        Assert.equals(Week.TUESDAY, Week.of("tuesday"));

        Assert.equals(Week.WEDNESDAY, Week.of("wed"));
        Assert.equals(Week.WEDNESDAY, Week.of("WEDNESDAY"));

        Assert.equals(Week.THURSDAY, Week.of("thu"));
        Assert.equals(Week.THURSDAY, Week.of("THURSDAY"));

        Assert.equals(Week.FRIDAY, Week.of("fri"));
        Assert.equals(Week.FRIDAY, Week.of("FRIDAY"));

        Assert.equals(Week.SATURDAY, Week.of("sat"));
        Assert.equals(Week.SATURDAY, Week.of("SATURDAY"));
    }

    @Test
    public void ofTest2() {
        Assert.equals(Week.SUNDAY, Week.of(DayOfWeek.SUNDAY));
        Assert.equals(Week.MONDAY, Week.of(DayOfWeek.MONDAY));
        Assert.equals(Week.TUESDAY, Week.of(DayOfWeek.TUESDAY));
        Assert.equals(Week.WEDNESDAY, Week.of(DayOfWeek.WEDNESDAY));
        Assert.equals(Week.THURSDAY, Week.of(DayOfWeek.THURSDAY));
        Assert.equals(Week.FRIDAY, Week.of(DayOfWeek.FRIDAY));
        Assert.equals(Week.SATURDAY, Week.of(DayOfWeek.SATURDAY));
    }

    @Test
    public void toJdkDayOfWeekTest() {
        Assert.equals(DayOfWeek.MONDAY, Week.MONDAY.toJdkDayOfWeek());
        Assert.equals(DayOfWeek.TUESDAY, Week.TUESDAY.toJdkDayOfWeek());
        Assert.equals(DayOfWeek.WEDNESDAY, Week.WEDNESDAY.toJdkDayOfWeek());
        Assert.equals(DayOfWeek.THURSDAY, Week.THURSDAY.toJdkDayOfWeek());
        Assert.equals(DayOfWeek.FRIDAY, Week.FRIDAY.toJdkDayOfWeek());
        Assert.equals(DayOfWeek.SATURDAY, Week.SATURDAY.toJdkDayOfWeek());
        Assert.equals(DayOfWeek.SUNDAY, Week.SUNDAY.toJdkDayOfWeek());
    }
}
