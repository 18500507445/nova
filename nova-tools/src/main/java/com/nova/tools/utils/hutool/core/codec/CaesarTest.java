package com.nova.tools.utils.hutool.core.codec;

import cn.hutool.core.codec.Caesar;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class CaesarTest {

    @Test
    public void caesarTest() {
        String str = "1f2e9df6131b480b9fdddc633cf24996";

        String encode = Caesar.encode(str, 3);
        Assert.equals("1H2G9FH6131D480D9HFFFE633EH24996", encode);

        String decode = Caesar.decode(encode, 3);
        Assert.equals(str, decode);
    }
}
