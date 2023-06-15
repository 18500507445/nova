package com.nova.tools.utils.hutool.core.lang.hash;

import cn.hutool.core.lang.hash.CityHash;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class CityHashTest {

    @Test
    public void hash32Test() {
        int hv = CityHash.hash32(StrUtil.utf8Bytes("你"));
        Assert.equals(1290029860, hv);

        hv = CityHash.hash32(StrUtil.utf8Bytes("你好"));
        Assert.equals(1374181357, hv);

        hv = CityHash.hash32(StrUtil.utf8Bytes("见到你很高兴"));
        Assert.equals(1475516842, hv);
        hv = CityHash.hash32(StrUtil.utf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
        Assert.equals(0x51020cae, hv);
    }

    @Test
    public void hash64Test() {
        long hv = CityHash.hash64(StrUtil.utf8Bytes("你"));
        Assert.equals(-4296898700418225525L, hv);

        hv = CityHash.hash64(StrUtil.utf8Bytes("你好"));
        Assert.equals(-4294276205456761303L, hv);

        hv = CityHash.hash64(StrUtil.utf8Bytes("见到你很高兴"));
        Assert.equals(272351505337503793L, hv);
        hv = CityHash.hash64(StrUtil.utf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
        Assert.equals(-8234735310919228703L, hv);
    }
}
