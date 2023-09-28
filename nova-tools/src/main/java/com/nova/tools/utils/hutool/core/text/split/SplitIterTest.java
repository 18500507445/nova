package com.nova.tools.utils.hutool.core.text.split;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.finder.CharFinder;
import cn.hutool.core.text.finder.LengthFinder;
import cn.hutool.core.text.finder.PatternFinder;
import cn.hutool.core.text.finder.StrFinder;
import cn.hutool.core.text.split.SplitIter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

public class SplitIterTest {

    @Test
    public void splitByCharTest() {
        String str1 = "a, ,,efedsfs,   ddf,";

        //不忽略""
        SplitIter splitIter = new SplitIter(str1,
                new CharFinder(',', false),
                Integer.MAX_VALUE,
                false
        );
        Assert.equals(6, splitIter.toList(false).size());
    }

    @Test
    public void splitByCharIgnoreCaseTest() {
        String str1 = "a, ,,eAedsas,   ddf,";

        //不忽略""
        SplitIter splitIter = new SplitIter(str1,
                new CharFinder('a', true),
                Integer.MAX_VALUE,
                false
        );
        Assert.equals(4, splitIter.toList(false).size());
    }

    @Test
    public void splitByCharIgnoreEmptyTest() {
        String str1 = "a, ,,efedsfs,   ddf,";

        SplitIter splitIter = new SplitIter(str1,
                new CharFinder(',', false),
                Integer.MAX_VALUE,
                true
        );

        final List<String> strings = splitIter.toList(false);
        Assert.equals(4, strings.size());
    }

    @Test
    public void splitByCharTrimTest() {
        String str1 = "a, ,,efedsfs,   ddf,";

        SplitIter splitIter = new SplitIter(str1,
                new CharFinder(',', false),
                Integer.MAX_VALUE,
                true
        );

        final List<String> strings = splitIter.toList(true);
        Assert.equals(3, strings.size());
        Assert.equals("a", strings.get(0));
        Assert.equals("efedsfs", strings.get(1));
        Assert.equals("ddf", strings.get(2));
    }

    @Test
    public void splitByStrTest() {
        String str1 = "a, ,,efedsfs,   ddf,";

        SplitIter splitIter = new SplitIter(str1,
                new StrFinder("e", false),
                Integer.MAX_VALUE,
                true
        );

        final List<String> strings = splitIter.toList(false);
        Assert.equals(3, strings.size());
    }

    @Test
    public void splitByPatternTest() {
        String str1 = "a, ,,efedsfs,   ddf,";

        SplitIter splitIter = new SplitIter(str1,
                new PatternFinder(Pattern.compile("\\s")),
                Integer.MAX_VALUE,
                true
        );

        final List<String> strings = splitIter.toList(false);
        Assert.equals(3, strings.size());
    }

    @Test
    public void splitByLengthTest() {
        String text = "1234123412341234";
        SplitIter splitIter = new SplitIter(text,
                new LengthFinder(4),
                Integer.MAX_VALUE,
                false
        );

        final List<String> strings = splitIter.toList(false);
        Assert.equals(4, strings.size());
    }

    @Test
    public void splitLimitTest() {
        String text = "55:02:18";
        SplitIter splitIter = new SplitIter(text,
                new CharFinder(':'),
                3,
                false
        );

        final List<String> strings = splitIter.toList(false);
        Assert.equals(3, strings.size());
    }

    @Test
    public void splitToSingleTest() {
        String text = "";
        SplitIter splitIter = new SplitIter(text,
                new CharFinder(':'),
                3,
                false
        );

        final List<String> strings = splitIter.toList(false);
        Assert.equals(1, strings.size());
    }

    // 切割字符串是空字符串时报错
    @Test
    public void splitByEmptyTest() {
        String text = "aa,bb,cc";
        SplitIter splitIter = new SplitIter(text,
                new StrFinder("", false),
                3,
                false
        );

        final List<String> strings = splitIter.toList(false);
        Assert.equals(1, strings.size());
    }
}
