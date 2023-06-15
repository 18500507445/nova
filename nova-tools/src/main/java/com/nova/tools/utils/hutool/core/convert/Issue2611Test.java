package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class Issue2611Test {

    @Test
    public void chineseMoneyToNumberTest() {
        final BigDecimal value = Convert.chineseMoneyToNumber("陆万柒仟伍佰伍拾柒元");

        Assert.equals("67,557.00", NumberUtil.decimalFormatMoney(value.doubleValue()));
    }
}
