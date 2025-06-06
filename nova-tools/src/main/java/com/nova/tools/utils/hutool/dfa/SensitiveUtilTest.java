package com.nova.tools.utils.hutool.dfa;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.dfa.SensitiveUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 敏感词过滤，DFA算法
 *
 * @author: Looly
 */
public class SensitiveUtilTest {

    @Test
    public void testSensitiveFilter() {
        List<String> wordList = new ArrayList<>();
        wordList.add("大");
        wordList.add("大土豆");
        wordList.add("土豆");
        wordList.add("刚出锅");
        wordList.add("出锅");
        TestBean bean = new TestBean();
        bean.setStr("我有一颗$大土^豆，刚出锅的");
        bean.setNum(100);
        SensitiveUtil.init(wordList);
        bean = SensitiveUtil.sensitiveFilter(bean, true, null);
        Assert.equals(bean.getStr(), "我有一颗$****，***的");
    }

    @Data
    public static class TestBean {
        private String str;
        private Integer num;
    }

    @Test
    public void issue2126() {
        SensitiveUtil.init(ListUtil.of("赵", "赵阿", "赵阿三"));

        String result = SensitiveUtil.sensitiveFilter("赵阿三在做什么。", true, null);
        Assert.equals("***在做什么。", result);
    }

    @Test
    public void demoA() {
        SensitiveUtil.init(ListUtil.of("赵", "赵阿", "赵阿三"));
        String s = SensitiveUtil.sensitiveFilter("你好，赵阿三在做什么。");
        System.err.println("s = " + s);
    }

}
