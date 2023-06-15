package com.nova.tools.utils.hutool.core.text;

import cn.hutool.core.text.TextSimilarity;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * 文本相似度计算工具类单元测试
 *
 * @author looly
 */
public class TextSimilarityTest {

    @Test
    public void similarDegreeTest() {
        String a = "我是一个文本，独一无二的文本";
        String b = "一个文本，独一无二的文本";

        double degree = TextSimilarity.similar(a, b);
        Assert.equals(0.8461538462D, degree);

        String similarPercent = TextSimilarity.similar(a, b, 2);
        Assert.equals("84.62%", similarPercent);
    }

    @Test
    public void similarDegreeTest2() {
        String a = "我是一个文本，独一无二的文本";
        String b = "一个文本，独一无二的文本,#,>>?#$%^%$&^&^%";

        double degree = TextSimilarity.similar(a, b);
        Assert.equals(0.8461538462D, degree);

        String similarPercent = TextSimilarity.similar(a, b, 2);
        Assert.equals("84.62%", similarPercent);
    }

    @Test
    public void similarTest() {
        final double abd = TextSimilarity.similar("abd", "1111");
        Assert.equals(0, abd);
    }
}
