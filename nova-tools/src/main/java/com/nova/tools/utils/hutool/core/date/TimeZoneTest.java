package com.nova.tools.utils.hutool.core.date;

import java.util.TimeZone;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import cn.hutool.core.date.format.FastDateFormat;

public class TimeZoneTest {

    @Test
    public void timeZoneConvertTest() {
        DateTime dt = DateUtil.parse("2018-07-10 21:44:32", //
                FastDateFormat.getInstance(DatePattern.NORM_DATETIME_PATTERN, TimeZone.getTimeZone("GMT+8:00")));
        Assert.equals("2018-07-10 21:44:32", dt.toString());

        dt.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        int hour = dt.getField(DateField.HOUR_OF_DAY);
        Assert.equals(14, hour);
        Assert.equals("2018-07-10 14:44:32", dt.toString());
    }
}
