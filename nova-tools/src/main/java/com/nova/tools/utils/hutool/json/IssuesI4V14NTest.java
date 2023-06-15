package com.nova.tools.utils.hutool.json;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IssuesI4V14NTest {

    @Test
    public void parseTest() {
        String str = "{\"A\" : \"A\\nb\"}";
        final JSONObject jsonObject = JSONUtil.parseObj(str);
        Assert.equals("A\nb", jsonObject.getStr("A"));

        final Map<String, String> map = jsonObject.toBean(new TypeReference<Map<String, String>>() {
        });
        Assert.equals("A\nb", map.get("A"));
    }
}
