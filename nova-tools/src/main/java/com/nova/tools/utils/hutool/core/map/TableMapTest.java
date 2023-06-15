package com.nova.tools.utils.hutool.core.map;

import cn.hutool.core.map.TableMap;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class TableMapTest {

    @Test
    public void getTest() {
        final TableMap<String, Integer> tableMap = new TableMap<>(16);
        tableMap.put("aaa", 111);
        tableMap.put("bbb", 222);

        Assert.equals(new Integer(111), tableMap.get("aaa"));
        Assert.equals(new Integer(222), tableMap.get("bbb"));

        Assert.equals("aaa", tableMap.getKey(111));
        Assert.equals("bbb", tableMap.getKey(222));
    }

    @SuppressWarnings("OverwrittenKey")
    @Test
    public void removeTest() {
        final TableMap<String, Integer> tableMap = new TableMap<>(16);
        tableMap.put("a", 111);
        tableMap.put("a", 222);
        tableMap.put("a", 222);

        tableMap.remove("a");

        Assert.equals(0, tableMap.size());
    }

    @SuppressWarnings("OverwrittenKey")
    @Test
    public void removeTest2() {
        final TableMap<String, Integer> tableMap = new TableMap<>(16);
        tableMap.put("a", 111);
        tableMap.put("a", 222);
        tableMap.put("a", 222);

        tableMap.remove("a", 222);

        Assert.equals(1, tableMap.size());
    }
}
