package com.nova.tools.utils.hutool.core.text.finder;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.finder.CharFinder;
import org.junit.jupiter.api.Test;

public class CharFinderTest {

    @Test
    public void startTest() {
        int start = new CharFinder('a').setText("cba123").start(2);
        Assert.equals(2, start);

        start = new CharFinder('c').setText("cba123").start(2);
        Assert.equals(-1, start);

        start = new CharFinder('3').setText("cba123").start(2);
        Assert.equals(5, start);
    }

    @Test
    public void negativeStartTest() {
        int start = new CharFinder('a').setText("cba123").setNegative(true).start(2);
        Assert.equals(2, start);

        start = new CharFinder('2').setText("cba123").setNegative(true).start(2);
        Assert.equals(-1, start);

        start = new CharFinder('c').setText("cba123").setNegative(true).start(2);
        Assert.equals(0, start);
    }
}
