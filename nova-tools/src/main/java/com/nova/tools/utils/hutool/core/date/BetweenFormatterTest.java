package com.nova.tools.utils.hutool.core.date;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.BetweenFormatter.Level;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class BetweenFormatterTest {

    @Test
    public void formatTest() {
        long betweenMs = DateUtil.betweenMs(DateUtil.parse("2017-01-01 22:59:59"), DateUtil.parse("2017-01-02 23:59:58"));
        BetweenFormatter formater = new BetweenFormatter(betweenMs, Level.MILLISECOND, 1);
        Assert.equals(formater.toString(), "1天");
    }

    @Test
    public void formatBetweenTest() {
        long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 11:23:19"), DateUtil.parse("2018-07-16 11:23:20"));
        BetweenFormatter formater = new BetweenFormatter(betweenMs, Level.SECOND, 1);
        Assert.equals(formater.toString(), "1秒");
    }

    @Test
    public void formatBetweenTest2() {
        long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 12:25:23"), DateUtil.parse("2018-07-16 11:23:20"));
        BetweenFormatter formater = new BetweenFormatter(betweenMs, Level.SECOND, 5);
        Assert.equals(formater.toString(), "1小时2分3秒");
    }

    @Test
    public void formatTest2() {
        BetweenFormatter formater = new BetweenFormatter(584, Level.SECOND, 1);
        Assert.equals(formater.toString(), "0秒");
    }
}
