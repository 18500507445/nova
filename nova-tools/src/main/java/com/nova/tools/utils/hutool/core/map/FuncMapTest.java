package com.nova.tools.utils.hutool.core.map;

import cn.hutool.core.map.FuncMap;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class FuncMapTest {

    @Test
    public void putGetTest() {
        final FuncMap<Object, Object> map = new FuncMap<>(HashMap::new,
                (key) -> key.toString().toLowerCase(),
                (value) -> value.toString().toUpperCase());

        map.put("aaa", "b");
        map.put("BBB", "c");

        Assert.equals("B", map.get("aaa"));
        Assert.equals("C", map.get("bbb"));
    }
}
