package com.nova.tools.utils.hutool.json;

import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class JSONNullTest {

    @Test
    public void parseNullTest() {
        JSONObject bodyjson = JSONUtil.parseObj("{\n" +
                "            \"device_model\": null,\n" +
                "            \"device_status_date\": null,\n" +
                "            \"imsi\": null,\n" +
                "            \"act_date\": \"2021-07-23T06:23:26.000+00:00\"}");
        Assert.equals(JSONNull.class, bodyjson.get("device_model").getClass());
        Assert.equals(JSONNull.class, bodyjson.get("device_status_date").getClass());
        Assert.equals(JSONNull.class, bodyjson.get("imsi").getClass());

        bodyjson.getConfig().setIgnoreNullValue(true);
        Assert.equals("{\"act_date\":\"2021-07-23T06:23:26.000+00:00\"}", bodyjson.toString());
    }

    @Test
    public void parseNullTest2() {
        JSONObject bodyjson = JSONUtil.parseObj("{\n" +
                "            \"device_model\": null,\n" +
                "            \"device_status_date\": null,\n" +
                "            \"imsi\": null,\n" +
                "            \"act_date\": \"2021-07-23T06:23:26.000+00:00\"}", true);
        Assert.isFalse(bodyjson.containsKey("device_model"));
        Assert.isFalse(bodyjson.containsKey("device_status_date"));
        Assert.isFalse(bodyjson.containsKey("imsi"));
    }
}
