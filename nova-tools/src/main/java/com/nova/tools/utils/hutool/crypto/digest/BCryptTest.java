package com.nova.tools.utils.hutool.crypto.digest;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.digest.BCrypt;
import org.junit.jupiter.api.Test;

public class BCryptTest {

    @Test
    public void checkpwTest() {
        Assert.isFalse(BCrypt.checkpw("xxx",
                "$2a$2a$10$e4lBTlZ019KhuAFyqAlgB.Jxc6cM66GwkSR/5/xXNQuHUItPLyhzy"));
    }
}
