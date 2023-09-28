package com.nova.tools.utils.hutool.extra.pinyin;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.pinyin.engine.houbbpinyin.HoubbPinyinEngine;
import org.junit.jupiter.api.Test;

public class HoubbPinyinTest {

    final HoubbPinyinEngine engine = new HoubbPinyinEngine();

    @Test
    public void getFirstLetterTest() {
        final String result = engine.getFirstLetter("林海", "");
        Assert.equals("lh", result);
    }

    @Test
    public void getPinyinTest() {
        final String pinyin = engine.getPinyin("你好h", " ");
        Assert.equals("ni hao h", pinyin);
    }
}
