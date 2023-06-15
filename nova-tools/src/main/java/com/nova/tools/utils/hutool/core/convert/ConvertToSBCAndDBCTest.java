package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * 类型转换工具单元测试
 * 全角半角转换
 *
 * @author Looly
 */
public class ConvertToSBCAndDBCTest {

    @Test
    public void toSBCTest() {
        String a = "123456789";
        String sbc = Convert.toSBC(a);
        Assert.equals("１２３４５６７８９", sbc);
    }

    @Test
    public void toDBCTest() {
        String a = "１２３４５６７８９";
        String dbc = Convert.toDBC(a);
        Assert.equals("123456789", dbc);
    }
}
