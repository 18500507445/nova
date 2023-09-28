package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import org.junit.jupiter.api.Test;

/**
 * {@link BooleanUtil} 布尔类型工具 测试类
 *
 * @author
 */
public class BooleanUtilTest {

    @Test
    public void toBooleanTest() {
        Assert.isTrue(BooleanUtil.toBoolean("true"));
        Assert.isTrue(BooleanUtil.toBoolean("yes"));
        Assert.isTrue(BooleanUtil.toBoolean("t"));
        Assert.isTrue(BooleanUtil.toBoolean("OK"));
        Assert.isTrue(BooleanUtil.toBoolean("1"));
        Assert.isTrue(BooleanUtil.toBoolean("On"));
        Assert.isTrue(BooleanUtil.toBoolean("是"));
        Assert.isTrue(BooleanUtil.toBoolean("对"));
        Assert.isTrue(BooleanUtil.toBoolean("真"));

        Assert.isFalse(BooleanUtil.toBoolean("false"));
        Assert.isFalse(BooleanUtil.toBoolean("6455434"));
        Assert.isFalse(BooleanUtil.toBoolean(""));
    }

    @Test
    public void andTest() {
        Assert.isFalse(BooleanUtil.and(true, false));
        Assert.isFalse(BooleanUtil.andOfWrap(true, false));
    }

    @Test
    public void orTest() {
        Assert.isTrue(BooleanUtil.or(true, false));
        Assert.isTrue(BooleanUtil.orOfWrap(true, false));
    }

    @Test
    public void xorTest() {
        Assert.isTrue(BooleanUtil.xor(true, false));
        Assert.isTrue(BooleanUtil.xorOfWrap(true, false));
    }

    public void orOfWrapTest() {
        Assert.isFalse(BooleanUtil.orOfWrap(Boolean.FALSE, null));
        Assert.isTrue(BooleanUtil.orOfWrap(Boolean.TRUE, null));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void isTrueIsFalseTest() {
        Assert.isFalse(BooleanUtil.isTrue(null));
        Assert.isFalse(BooleanUtil.isFalse(null));
    }

    @SuppressWarnings("ConstantConditions")
    public void negateTest() {
        Assert.isFalse(BooleanUtil.negate(Boolean.TRUE));
        Assert.isTrue(BooleanUtil.negate(Boolean.FALSE));

        Assert.isFalse(BooleanUtil.negate(Boolean.TRUE.booleanValue()));
        Assert.isTrue(BooleanUtil.negate(Boolean.FALSE.booleanValue()));
    }

    @Test
    public void toStringTest() {
        Assert.equals("true", BooleanUtil.toStringTrueFalse(true));
        Assert.equals("false", BooleanUtil.toStringTrueFalse(false));

        Assert.equals("yes", BooleanUtil.toStringYesNo(true));
        Assert.equals("no", BooleanUtil.toStringYesNo(false));

        Assert.equals("on", BooleanUtil.toStringOnOff(true));
        Assert.equals("off", BooleanUtil.toStringOnOff(false));
    }
}
