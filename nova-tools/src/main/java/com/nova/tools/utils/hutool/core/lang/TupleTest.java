package com.nova.tools.utils.hutool.core.lang;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Tuple;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.TimeZone;

/**
 * 元祖测试类
 */
public class TupleTest {

    @Test
    public void hashCodeTest() {
        final Tuple tuple = new Tuple(Locale.getDefault(), TimeZone.getDefault());
        final Tuple tuple2 = new Tuple(Locale.getDefault(), TimeZone.getDefault());
        Assert.equals(tuple, tuple2);
    }
}
