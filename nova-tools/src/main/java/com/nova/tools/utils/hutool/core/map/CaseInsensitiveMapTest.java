package com.nova.tools.utils.hutool.core.map;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.CaseInsensitiveLinkedMap;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class CaseInsensitiveMapTest {

    @Test
    public void caseInsensitiveMapTest() {
        CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>();
        map.put("aAA", "OK");
        Assert.equals("OK", map.get("aaa"));
        Assert.equals("OK", map.get("AAA"));
    }

    @Test
    public void caseInsensitiveLinkedMapTest() {
        CaseInsensitiveLinkedMap<String, String> map = new CaseInsensitiveLinkedMap<>();
        map.put("aAA", "OK");
        Assert.equals("OK", map.get("aaa"));
        Assert.equals("OK", map.get("AAA"));
    }

    @Test
    public void mergeTest() {
        //https://github.com/dromara/hutool/issues/2086
        Pair<String, String> b = new Pair<>("a", "value");
        Pair<String, String> a = new Pair<>("A", "value");
        final CaseInsensitiveMap<Object, Object> map = new CaseInsensitiveMap<>();
        map.merge(b.getKey(), b.getValue(), (A, B) -> A);
        map.merge(a.getKey(), a.getValue(), (A, B) -> A);

        Assert.equals(1, map.size());
    }
}
