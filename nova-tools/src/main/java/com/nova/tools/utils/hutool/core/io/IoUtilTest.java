package com.nova.tools.utils.hutool.core.io;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

public class IoUtilTest {

    @Test
    public void readBytesTest() {
        final byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream("hutool.jpg"));
        Assert.equals(22807, bytes.length);
    }

    @Test
    public void readBytesWithLengthTest() {
        // 读取固定长度
        final int limit = RandomUtil.randomInt(22807);
        final byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream("hutool.jpg"), limit);
        Assert.equals(limit, bytes.length);
    }

    @Test
    public void readLinesTest() {
        try (BufferedReader reader = ResourceUtil.getUtf8Reader("test_lines.csv");) {
            IoUtil.readLines(reader, (LineHandler) Assert::notNull);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
