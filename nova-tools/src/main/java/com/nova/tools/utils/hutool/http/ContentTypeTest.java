package com.nova.tools.utils.hutool.http;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.ContentType;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * ContentType 单元测试
 */
public class ContentTypeTest {

    @Test
    public void testBuild() {
        String result = ContentType.build(ContentType.JSON, CharsetUtil.CHARSET_UTF_8);
        Assert.equals("application/json;charset=UTF-8", result);
    }
}
