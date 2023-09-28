package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.CastUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.*;

public class CastUtilTest {

    @Test
    public void testCastToSuper() {
        Collection<Integer> collection = CollUtil.newLinkedList(1, 2, 3);
        List<Integer> list = CollUtil.newArrayList(1, 2, 3);
        Set<Integer> set = CollUtil.newHashSet(1, 2, 3);
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 1);

        Collection<Number> collection2 = CastUtil.castUp(collection);
        Assert.equals(collection, collection2);

        Collection<Integer> collection3 = CastUtil.castDown(collection2);
        Assert.equals(collection2, collection3);

        List<Number> list2 = CastUtil.castUp(list);
        Assert.equals(list, list2);
        List<Integer> list3 = CastUtil.castDown(list2);
        Assert.equals(list2, list3);

        Set<Number> set2 = CastUtil.castUp(set);
        Assert.equals(set, set2);
        Set<Integer> set3 = CastUtil.castDown(set2);
        Assert.equals(set2, set3);

        Map<Number, Serializable> map2 = CastUtil.castUp(map);
        Assert.equals(map, map2);
        Map<Integer, Number> map3 = CastUtil.castDown(map2);
        Assert.equals(map2, map3);
    }
}
