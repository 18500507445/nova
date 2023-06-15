package com.nova.tools.utils.hutool.core.lang.intern;

import cn.hutool.core.lang.intern.InternUtil;
import cn.hutool.core.lang.intern.Interner;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class InternUtilTest {

    /**
     * 检查规范字符串是否相同
     */
    @SuppressWarnings("StringOperationCanBeSimplified")
    @Test
    public void weakTest() {
        final Interner<String> interner = InternUtil.createWeakInterner();
        String a1 = RandomUtil.randomString(RandomUtil.randomInt(100));
        String a2 = new String(a1);

        Assert.equals(a1, a2);

        Assert.equals(interner.intern(a1), interner.intern(a2));
    }

}
