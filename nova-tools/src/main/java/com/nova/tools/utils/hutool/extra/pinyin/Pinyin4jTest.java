package com.nova.tools.utils.hutool.extra.pinyin;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.pinyin.engine.pinyin4j.Pinyin4jEngine;
import org.junit.jupiter.api.Test;

public class Pinyin4jTest {

    final Pinyin4jEngine engine = new Pinyin4jEngine();

    @Test
    public void getFirstLetterByPinyin4jTest() {
        final String result = engine.getFirstLetter("林海", "");
        Assert.equals("lh", result);
    }

    @Test
    public void getPinyinByPinyin4jTest() {
        final String pinyin = engine.getPinyin("你好h", " ");
        Assert.equals("ni hao h", pinyin);
    }
}
