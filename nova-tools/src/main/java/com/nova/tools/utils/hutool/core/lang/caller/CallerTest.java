package com.nova.tools.utils.hutool.core.lang.caller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.caller.CallerUtil;
import org.junit.jupiter.api.Test;

/**
 * {@link CallerUtil} 单元测试
 *
 * @author: Looly
 */
public class CallerTest {

    @Test
    public void getCallerTest() {
        Class<?> caller = CallerUtil.getCaller();
        Assert.equals(this.getClass(), caller);

        Class<?> caller0 = CallerUtil.getCaller(0);
        Assert.equals(CallerUtil.class, caller0);

        Class<?> caller1 = CallerUtil.getCaller(1);
        Assert.equals(this.getClass(), caller1);
    }

    @Test
    public void getCallerCallerTest() {
        Class<?> callerCaller = CallerTestClass.getCaller();
        Assert.equals(this.getClass(), callerCaller);
    }

    private static class CallerTestClass {
        public static Class<?> getCaller() {
            return CallerUtil.getCallerCaller();
        }
    }
}
