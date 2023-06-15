package com.nova.tools.utils.hutool.core.date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class Issue2612Test {

    @Test
    public void parseTest() {
        Assert.equals("2022-09-14 23:59:00",
                Objects.requireNonNull(DateUtil.parse("2022-09-14T23:59:00-08:00")).toString());

        Assert.equals("2022-09-14 23:59:00",
                Objects.requireNonNull(DateUtil.parse("2022-09-14T23:59:00-0800")).toString());
    }
}
