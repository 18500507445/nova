package com.nova.tools.utils.hutool.core.exceptions;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * 异常工具单元测试
 *
 * @author looly
 */
public class ExceptionUtilTest {

    @Test
    public void wrapTest() {
        IORuntimeException e = ExceptionUtil.wrap(new IOException(), IORuntimeException.class);
        Assert.notNull(e);
    }

    @Test
    public void getRootTest() {
        // 查找入口方法
        StackTraceElement ele = ExceptionUtil.getRootStackElement();
        Assert.equals("main", ele.getMethodName());
    }

    @Test
    public void convertTest() {
        // RuntimeException e = new RuntimeException();
        IOException ioException = new IOException();
        IllegalArgumentException argumentException = new IllegalArgumentException(ioException);
        IOException ioException1 = ExceptionUtil.convertFromOrSuppressedThrowable(argumentException, IOException.class, true);
        Assert.notNull(ioException1);
    }

    @Test
    public void bytesIntConvertTest() {
        final String s = Convert.toStr(12);
        final int integer = Convert.toInt(s);
        Assert.equals(12, integer);

        final byte[] bytes = Convert.intToBytes(12);
        final int i = Convert.bytesToInt(bytes);
        Assert.equals(12, i);
    }
}
