package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ModifierUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * {@link ModifierUtil} 修饰符工具类
 *
 * @author
 */
public class ModifierUtilTest {

    @Test
    public void hasModifierTest() throws NoSuchMethodException {
        Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");
        Assert.isTrue(ModifierUtil.hasModifier(method, ModifierUtil.ModifierType.PRIVATE));
        Assert.isTrue(ModifierUtil.hasModifier(method,
                ModifierUtil.ModifierType.PRIVATE,
                ModifierUtil.ModifierType.STATIC)
        );
    }

    private static void ddd() {
    }
}
