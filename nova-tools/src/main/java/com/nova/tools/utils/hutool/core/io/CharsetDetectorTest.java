package com.nova.tools.utils.hutool.core.io;

import cn.hutool.core.io.CharsetDetector;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.lang.Assert;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

public class CharsetDetectorTest {

    @Test
    public void detectTest() {
        // 测试多个Charset对同一个流的处理是否有问题
        final Charset detect = CharsetDetector.detect(ResourceUtil.getStream("test.xml"),
                CharsetUtil.CHARSET_GBK, CharsetUtil.CHARSET_UTF_8);
        Assert.equals(CharsetUtil.CHARSET_UTF_8, detect);
    }

    @Test
    @Ignore
    public void issue2547() {
        final Charset detect = CharsetDetector.detect(IoUtil.DEFAULT_LARGE_BUFFER_SIZE,
                ResourceUtil.getStream("d:/test/default.txt"));
        Assert.equals(CharsetUtil.CHARSET_UTF_8, detect);
    }
}
