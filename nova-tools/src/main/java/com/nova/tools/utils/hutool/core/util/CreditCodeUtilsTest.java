package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CreditCodeUtil;
import org.junit.jupiter.api.Test;

/**
 * {@link CreditCodeUtil} 统一社会信用代码 测试类
 *
 * @author
 */
public class CreditCodeUtilsTest {

    @Test
    public void isCreditCodeBySimple() {
        String testCreditCode = "91310115591693856A";
        Assert.isTrue(CreditCodeUtil.isCreditCodeSimple(testCreditCode));
    }

    @Test
    public void isCreditCode() {
        String testCreditCode = "91310110666007217T";
        Assert.isTrue(CreditCodeUtil.isCreditCode(testCreditCode));
    }

    @Test
    public void isCreditCode2() {
        // 由于早期部分试点地区推行 法人和其他组织统一社会信用代码 较早，会存在部分代码不符合国家标准的情况。
        // 见：https://github.com/bluesky335/IDCheck
        String testCreditCode = "91350211M00013FA1N";
        Assert.isFalse(CreditCodeUtil.isCreditCode(testCreditCode));
    }

    @Test
    public void randomCreditCode() {
        final String s = CreditCodeUtil.randomCreditCode();
        Assert.isTrue(CreditCodeUtil.isCreditCode(s));
    }
}
