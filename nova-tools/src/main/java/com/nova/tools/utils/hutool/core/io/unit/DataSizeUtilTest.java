package com.nova.tools.utils.hutool.core.io.unit;

import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class DataSizeUtilTest {

    @Test
    public void parseTest() {
        long parse = DataSizeUtil.parse("3M");
        Assert.equals(3145728, parse);

        parse = DataSizeUtil.parse("3m");
        Assert.equals(3145728, parse);

        parse = DataSizeUtil.parse("3MB");
        Assert.equals(3145728, parse);

        parse = DataSizeUtil.parse("3mb");
        Assert.equals(3145728, parse);

        parse = DataSizeUtil.parse("3.1M");
        Assert.equals(3250585, parse);

        parse = DataSizeUtil.parse("3.1m");
        Assert.equals(3250585, parse);

        parse = DataSizeUtil.parse("3.1MB");
        Assert.equals(3250585, parse);

        parse = DataSizeUtil.parse("-3.1MB");
        Assert.equals(-3250585, parse);

        parse = DataSizeUtil.parse("+3.1MB");
        Assert.equals(3250585, parse);

        parse = DataSizeUtil.parse("3.1mb");
        Assert.equals(3250585, parse);

        parse = DataSizeUtil.parse("3.1");
        Assert.equals(3, parse);

        try {
            DataSizeUtil.parse("3.1.3");
        } catch (IllegalArgumentException ie) {
            Assert.equals("'3.1.3' is not a valid data size", ie.getMessage());
        }


    }

    @Test
    public void formatTest() {
        String format = DataSizeUtil.format(Long.MAX_VALUE);
        Assert.equals("8 EB", format);

        format = DataSizeUtil.format(1024L * 1024 * 1024 * 1024 * 1024);
        Assert.equals("1 PB", format);

        format = DataSizeUtil.format(1024L * 1024 * 1024 * 1024);
        Assert.equals("1 TB", format);
    }
}
