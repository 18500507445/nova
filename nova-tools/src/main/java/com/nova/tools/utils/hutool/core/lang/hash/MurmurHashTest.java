package com.nova.tools.utils.hutool.core.lang.hash;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.hash.MurmurHash;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

public class MurmurHashTest {

    @Test
    public void hash32Test() {
        int hv = MurmurHash.hash32(StrUtil.utf8Bytes("你"));
        Assert.equals(-1898877446, hv);

        hv = MurmurHash.hash32(StrUtil.utf8Bytes("你好"));
        Assert.equals(337357348, hv);

        hv = MurmurHash.hash32(StrUtil.utf8Bytes("见到你很高兴"));
        Assert.equals(1101306141, hv);
        hv = MurmurHash.hash32(StrUtil.utf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
        Assert.equals(-785444229, hv);
    }

    @Test
    public void hash64Test() {
        long hv = MurmurHash.hash64(StrUtil.utf8Bytes("你"));
        Assert.equals(-1349759534971957051L, hv);

        hv = MurmurHash.hash64(StrUtil.utf8Bytes("你好"));
        Assert.equals(-7563732748897304996L, hv);

        hv = MurmurHash.hash64(StrUtil.utf8Bytes("见到你很高兴"));
        Assert.equals(-766658210119995316L, hv);
        hv = MurmurHash.hash64(StrUtil.utf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
        Assert.equals(-7469283059271653317L, hv);
    }
}
