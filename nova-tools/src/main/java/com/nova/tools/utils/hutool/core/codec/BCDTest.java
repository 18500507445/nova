package com.nova.tools.utils.hutool.core.codec;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class BCDTest {

    @Test
    public void bcdTest() {
        String strForTest = "123456ABCDEF";

        //转BCD
        byte[] bcd = BCD.strToBcd(strForTest);
        String str = BCD.bcdToStr(bcd);
        //解码BCD
        Assert.equals(strForTest, str);
    }
}
