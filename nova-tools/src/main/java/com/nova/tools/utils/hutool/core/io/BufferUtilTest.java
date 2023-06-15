package com.nova.tools.utils.hutool.core.io;

import java.nio.ByteBuffer;

import cn.hutool.core.io.BufferUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

/**
 * BufferUtil单元测试
 *
 * @author looly
 */
public class BufferUtilTest {

    @Test
    public void copyTest() {
        byte[] bytes = "AAABBB".getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        ByteBuffer buffer2 = BufferUtil.copy(buffer, ByteBuffer.allocate(5));
        Assert.equals("AAABB", StrUtil.utf8Str(buffer2));
    }

    @Test
    public void readBytesTest() {
        byte[] bytes = "AAABBB".getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        byte[] bs = BufferUtil.readBytes(buffer, 5);
        Assert.equals("AAABB", StrUtil.utf8Str(bs));
    }

    @Test
    public void readBytes2Test() {
        byte[] bytes = "AAABBB".getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        byte[] bs = BufferUtil.readBytes(buffer, 5);
        Assert.equals("AAABB", StrUtil.utf8Str(bs));
    }

    @Test
    public void readLineTest() {
        String text = "aa\r\nbbb\ncc";
        ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());

        // 第一行
        String line = BufferUtil.readLine(buffer, CharsetUtil.CHARSET_UTF_8);
        Assert.equals("aa", line);

        // 第二行
        line = BufferUtil.readLine(buffer, CharsetUtil.CHARSET_UTF_8);
        Assert.equals("bbb", line);

        // 第三行因为没有行结束标志，因此返回null
        line = BufferUtil.readLine(buffer, CharsetUtil.CHARSET_UTF_8);
        Assert.isNull(line);

        // 读取剩余部分
        Assert.equals("cc", StrUtil.utf8Str(BufferUtil.readBytes(buffer)));
    }
}
