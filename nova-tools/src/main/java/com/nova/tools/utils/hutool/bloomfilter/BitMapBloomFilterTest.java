package com.nova.tools.utils.hutool.bloomfilter;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.bitMap.IntMap;
import cn.hutool.bloomfilter.bitMap.LongMap;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * 布隆过滤器
 */
class BitMapBloomFilterTest {

    @Test
    public void filterTest() {
        BitMapBloomFilter filter = new BitMapBloomFilter(10);
        filter.add("123");
        filter.add("abc");
        filter.add("ddd");

        Assert.isTrue(filter.contains("abc"));
        Assert.isTrue(filter.contains("ddd"));
        Assert.isTrue(filter.contains("123"));
    }

    @Test
    public void testIntMap() {
        IntMap intMap = new IntMap();

        for (int i = 0; i < 32; i++) {
            intMap.add(i);
        }
        intMap.remove(30);


        for (int i = 0; i < 32; i++) {
            System.err.println(i + "是否存在-->" + intMap.contains(i));
        }
    }

    @Test
    public void testLongMap() {
        LongMap longMap = new LongMap();

        for (int i = 0; i < 64; i++) {
            longMap.add(i);
        }
        longMap.remove(30);


        for (int i = 0; i < 64; i++) {
            System.err.println(i + "是否存在-->" + longMap.contains(i));
        }
    }
}
