package com.nova.tools.utils.hutool.core.thread;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class ThreadUtilTest {

    @Test
    public void executeTest() {
        final boolean isValid = true;

        ThreadUtil.execute(() -> Assert.isTrue(isValid));
    }
}
