package com.nova.tools.utils.hutool.json;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class IssueI59LW4Test {

    @Test
    public void bytesTest() {
        final JSONObject jsonObject = JSONUtil.createObj().set("bytes", new byte[]{1});
        Assert.equals("{\"bytes\":[1]}", jsonObject.toString());

        final byte[] bytes = jsonObject.getBytes("bytes");
        Assert.equals(new byte[]{1}, bytes);
    }

    @Test
    public void bytesInJSONArrayTest() {
        final JSONArray jsonArray = JSONUtil.createArray().set(new byte[]{1});
        Assert.equals("[[1]]", jsonArray.toString());

        final byte[] bytes = jsonArray.getBytes(0);
        Assert.equals(new byte[]{1}, bytes);
    }
}
