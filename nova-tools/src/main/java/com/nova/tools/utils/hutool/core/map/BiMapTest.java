package com.nova.tools.utils.hutool.core.map;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.BiMap;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class BiMapTest {

    @Test
    public void getTest() {
        BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
        biMap.put("aaa", 111);
        biMap.put("bbb", 222);

        Assert.equals(new Integer(111), biMap.get("aaa"));
        Assert.equals(new Integer(222), biMap.get("bbb"));

        Assert.equals("aaa", biMap.getKey(111));
        Assert.equals("bbb", biMap.getKey(222));
    }
}
