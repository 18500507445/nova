package com.nova.tools.utils.hutool.core.math;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.math.Calculator;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    @Test
    public void conversationTest() {
        final double conversion = Calculator.conversion("(0*1--3)-5/-4-(3*(-2.13))");
        Assert.equals(10.64, conversion);
    }

    @Test
    public void conversationTest2() {
        final double conversion = Calculator.conversion("77 * 12");
        Assert.equals(924.0, conversion);
    }

    @Test
    public void conversationTest3() {
        final double conversion = Calculator.conversion("1");
        Assert.equals(1, conversion);
    }

    @Test
    public void conversationTest4() {
        final double conversion = Calculator.conversion("(88*66/23)%26+45%9");
        Assert.equals((88D * 66 / 23) % 26, conversion);
    }

    @Test
    public void conversationTest5() {
        // https://github.com/dromara/hutool/issues/1984
        final double conversion = Calculator.conversion("((1/1) / (1/1) -1) * 100");
        Assert.equals(0, conversion);
    }

    @Test
    public void conversationTest6() {
        final double conversion = Calculator.conversion("-((2.12-2) * 100)");
        Assert.equals(-1D * (2.12 - 2) * 100, conversion);
    }

    @Test
    public void conversationTest7() {
        //https://gitee.com/dromara/hutool/issues/I4KONB
        final double conversion = Calculator.conversion("((-2395+0) * 0.3+140.24+35+90)/30");
        Assert.equals(-15.11, conversion);
    }
}
