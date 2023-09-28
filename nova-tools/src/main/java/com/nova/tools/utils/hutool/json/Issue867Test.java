package com.nova.tools.utils.hutool.json;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class Issue867Test {

    @Test
    public void toBeanTest() {
        String json = "{\"abc_1d\":\"123\",\"abc_d\":\"456\",\"abc_de\":\"789\"}";
        Test02 bean = JSONUtil.toBean(JSONUtil.parseObj(json), Test02.class);
        Assert.equals("123", bean.getAbc1d());
        Assert.equals("456", bean.getAbcD());
        Assert.equals("789", bean.getAbcDe());
    }

    @Data
    static class Test02 {
        @Alias("abc_1d")
        private String abc1d;
        private String abcD;
        private String abcDe;
    }
}
