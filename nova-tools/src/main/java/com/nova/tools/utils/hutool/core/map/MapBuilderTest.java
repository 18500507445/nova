package com.nova.tools.utils.hutool.core.map;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapBuilder;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class MapBuilderTest {

    @Test
    public void conditionPutTest() {
        Map<String, String> map = MapBuilder.<String, String>create()
                .put(true, "a", "1")
                .put(false, "b", "2")
                .put(true, "c", () -> getValue(3))
                .put(false, "d", () -> getValue(4))
                .build();

        Assert.equals(map.get("a"), "1");
        Assert.isFalse(map.containsKey("b"));
        Assert.equals(map.get("c"), "3");
        Assert.isFalse(map.containsKey("d"));
    }

    public String getValue(int value) {
        return String.valueOf(value);
    }
}
