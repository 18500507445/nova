package com.nova.tools.utils.hutool.core.lang;

import cn.hutool.core.builder.GenericBuilder;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class DictTest {
    @Test
    public void dictTest() {
        Dict dict = Dict.create()
                .set("key1", 1)//int
                .set("key2", 1000L)//long
                .set("key3", DateTime.now());//Date

        Long v2 = dict.getLong("key2");
        Assert.equals(Long.valueOf(1000L), v2);
    }

    @Test
    public void dictTest2() {
        final Dict dict = new Dict(true);
        Map<String, Object> map = new HashMap<>();
        map.put("A", 1);

        dict.putAll(map);

        Assert.equals(1, dict.get("A"));
        Assert.equals(1, dict.get("a"));
    }

    @Test
    public void ofTest() {
        Dict dict = Dict.of(
                "RED", "#FF0000",
                "GREEN", "#00FF00",
                "BLUE", "#0000FF"
        );

        Assert.equals("#FF0000", dict.get("RED"));
        Assert.equals("#00FF00", dict.get("GREEN"));
        Assert.equals("#0000FF", dict.get("BLUE"));
    }

    @Test
    public void removeEqualTest() {
        Dict dict = Dict.of(
                "key1", null
        );

        Dict dict2 = Dict.of(
                "key1", null
        );

        dict.removeEqual(dict2);

        Assert.isTrue(dict.isEmpty());
    }

    @Test
    public void setFieldsTest() {
        OptTest.User user = GenericBuilder.of(OptTest.User::new).with(OptTest.User::setUsername, "hutool").build();
        Dict dict = Dict.create();
        dict.setFields(user::getNickname, user::getUsername);
        Assert.equals("hutool", dict.get("username"));
        Assert.isNull(dict.get("nickname"));
    }
}
