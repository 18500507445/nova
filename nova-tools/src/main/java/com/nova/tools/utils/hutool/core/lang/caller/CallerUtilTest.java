package com.nova.tools.utils.hutool.core.lang.caller;

import cn.hutool.core.lang.caller.CallerUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class CallerUtilTest {

    @Test
    public void getCallerMethodNameTest() {
        final String callerMethodName = CallerUtil.getCallerMethodName(false);
        Assert.equals("getCallerMethodNameTest", callerMethodName);

        final String fullCallerMethodName = CallerUtil.getCallerMethodName(true);
        Assert.equals("cn.hutool.core.lang.caller.CallerUtilTest.getCallerMethodNameTest", fullCallerMethodName);
    }
}