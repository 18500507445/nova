package com.nova.tools.utils.hutool.json;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONBeanParser;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class JSONBeanParserTest {

    @Test
    public void parseTest() {
        String jsonStr = "{\"customName\": \"customValue\", \"customAddress\": \"customAddressValue\"}";
        final TestBean testBean = JSONUtil.toBean(jsonStr, TestBean.class);
        Assert.notNull(testBean);
        Assert.equals("customValue", testBean.getName());
        Assert.equals("customAddressValue", testBean.getAddress());
    }

    @Data
    static class TestBean implements JSONBeanParser<JSONObject> {

        private String name;
        private String address;

        @Override
        public void parse(JSONObject value) {
            this.name = value.getStr("customName");
            this.address = value.getStr("customAddress");
        }
    }
}
