package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class PrimitiveConvertTest {

    @Test
    public void toIntTest() {
        final int convert = Convert.convert(int.class, "123");
        Assert.equals(123, convert);
    }

    @Test
    public void toIntErrorTest() {
        final int convert = Convert.convert(int.class, "aaaa");
    }
}
