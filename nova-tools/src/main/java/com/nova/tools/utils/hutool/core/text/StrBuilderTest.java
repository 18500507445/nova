package com.nova.tools.utils.hutool.core.text;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.lang.Assert;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;

/**
 * StrBuilder单元测试
 *
 * @author looly
 */
public class StrBuilderTest {

    /**
     * StrBuilder的性能测试
     */
    @Test
    @Ignore
    public void benchTest() {
        TimeInterval timer = DateUtil.timer();
        StrBuilder builder = StrBuilder.create();
        for (int i = 0; i < 1000000; i++) {
            builder.append("test");
            builder.reset();
        }
        Console.log(timer.interval());

        timer.restart();
        StringBuilder b2 = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            b2.append("test");
            b2 = new StringBuilder();
        }
        Console.log(timer.interval());
    }

    @Test
    public void appendTest() {
        StrBuilder builder = StrBuilder.create();
        builder.append("aaa").append("你好").append('r');
        Assert.equals("aaa你好r", builder.toString());
    }

    @Test
    public void insertTest() {
        StrBuilder builder = StrBuilder.create(1);
        builder.append("aaa").append("你好").append('r');
        builder.insert(3, "数据插入");
        Assert.equals("aaa数据插入你好r", builder.toString());
    }

    @Test
    public void insertTest2() {
        StrBuilder builder = StrBuilder.create(1);
        builder.append("aaa").append("你好").append('r');
        builder.insert(8, "数据插入");
        Assert.equals("aaa你好r  数据插入", builder.toString());
    }

    @Test
    public void resetTest() {
        StrBuilder builder = StrBuilder.create(1);
        builder.append("aaa").append("你好").append('r');
        builder.insert(3, "数据插入");
        builder.reset();
        Assert.equals("", builder.toString());
    }

    @Test
    public void resetTest2() {
        StrBuilder builder = StrBuilder.create(1);
        builder.append("aaa").append("你好").append('r');
        builder.insert(3, "数据插入");
        builder.reset();
        builder.append("bbb".toCharArray());
        Assert.equals("bbb", builder.toString());
    }

    @Test
    public void appendObjectTest() {
        StrBuilder builder = StrBuilder.create(1);
        builder.append(123).append(456.123D).append(true).append('\n');
        Assert.equals("123456.123true\n", builder.toString());
    }

    @Test
    public void delTest() {
        // 删除全部测试
        StrBuilder strBuilder = new StrBuilder("ABCDEFG");
        int length = strBuilder.length();
        StrBuilder builder = strBuilder.del(0, length);
        Assert.equals("", builder.toString());
    }

    @Test
    public void delTest2() {
        // 删除中间部分测试
        StrBuilder strBuilder = new StrBuilder("ABCDEFG");
        StrBuilder builder = strBuilder.del(2, 6);
        Assert.equals("ABG", builder.toString());
    }

    @Test
    public void delToTest() {
        StrBuilder strBuilder = new StrBuilder("ABCDEFG");

        // 不处理
        StrBuilder builder = strBuilder.delTo(7);
        Assert.equals("ABCDEFG", builder.toString());

        // 删除全部
        builder = strBuilder.delTo(0);
        Assert.equals("", builder.toString());
    }
}
