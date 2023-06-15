package com.nova.tools.utils.hutool.core.net;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class UrlDecoderTest {
    @Test
    public void decodeForPathTest() {
        Assert.equals("+", URLDecoder.decodeForPath("+", CharsetUtil.CHARSET_UTF_8));
    }
}
