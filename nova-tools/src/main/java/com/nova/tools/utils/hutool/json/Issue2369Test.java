package com.nova.tools.utils.hutool.json;

import cn.hutool.json.JSONUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class Issue2369Test {

    @Test
    public void toJsonStrTest() {
        //https://github.com/dromara/hutool/issues/2369
        // byte[]数组对于JSONArray来说，即可能是一个JSON字符串的二进制流，也可能是普通数组，因此需要做双向兼容
        final byte[] bytes = {10, 11};
        final String s = JSONUtil.toJsonStr(bytes);
        Assert.equals("[10,11]", s);

        final Object o = JSONUtil.toBean(s, byte[].class, false);
        Assert.equals(bytes, (byte[]) o);
    }
}
