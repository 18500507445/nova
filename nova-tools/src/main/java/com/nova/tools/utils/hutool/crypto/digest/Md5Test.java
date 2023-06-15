package com.nova.tools.utils.hutool.crypto.digest;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * MD5 单元测试
 *
 * @author Looly
 */
public class Md5Test {

    @Test
    public void md5To16Test() {
        String hex16 = new MD5().digestHex16("中国");
        Assert.equals(16, hex16.length());
        Assert.equals("cb143acd6c929826", hex16);
    }
}
