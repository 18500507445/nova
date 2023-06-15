package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class ConvertToBooleanTest {

    @Test
    public void intToBooleanTest() {
        int a = 100;
        final Boolean aBoolean = Convert.toBool(a);
        Assert.isTrue(aBoolean);

        int b = 0;
        final Boolean bBoolean = Convert.toBool(b);
        Assert.isFalse(bBoolean);
    }
}
