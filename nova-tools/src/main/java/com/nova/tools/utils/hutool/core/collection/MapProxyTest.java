package com.nova.tools.utils.hutool.core.collection;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapProxy;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapProxyTest {

    @Test
    public void mapProxyTest() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");

        MapProxy mapProxy = new MapProxy(map);
        Integer b = mapProxy.getInt("b");
        Assert.equals(new Integer(2), b);

        Set<Object> keys = mapProxy.keySet();
        Assert.isFalse(keys.isEmpty());

        Set<Entry<Object, Object>> entrys = mapProxy.entrySet();
        Assert.isFalse(entrys.isEmpty());
    }

    private interface Student {
        Student setName(String name);

        Student setAge(int age);

        String getName();

        int getAge();
    }

    @Test
    public void classProxyTest() {
        Student student = MapProxy.create(new HashMap<>()).toProxyBean(Student.class);
        student.setName("小明").setAge(18);
        Assert.equals(student.getAge(), 18);
        Assert.equals(student.getName(), "小明");
    }
}
