package com.nova.tools.utils.hutool.core.date;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.chinese.GanZhi;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class GanzhiTest {

    @Test
    public void getGanzhiOfYearTest() {
        Assert.equals("庚子", GanZhi.getGanzhiOfYear(2020));
    }

    @Test
    public void getCyclicalYMDTest() {
        //通过公历构建
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("1993-01-06"));
        String cyclicalYMD = chineseDate.getCyclicalYMD();
        Assert.equals("壬申年癸丑月丁亥日", cyclicalYMD);
    }

    @Test
    public void getCyclicalYMDTest2() {
        //通过农历构建
        ChineseDate chineseDate = new ChineseDate(1992, 12, 14);
        String cyclicalYMD = chineseDate.getCyclicalYMD();
        Assert.equals("壬申年癸丑月丁亥日", cyclicalYMD);
    }

    @Test
    public void getCyclicalYMDTest3() {
        //通过公历构建
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("2020-08-28"));
        String cyclicalYMD = chineseDate.getCyclicalYMD();
        Assert.equals("庚子年甲申月癸卯日", cyclicalYMD);
    }

    @Test
    public void getCyclicalYMDTest4() {
        //通过公历构建
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate("1905-08-28"));
        String cyclicalYMD = chineseDate.getCyclicalYMD();
        Assert.equals("乙巳年甲申月己亥日", cyclicalYMD);
    }
}
