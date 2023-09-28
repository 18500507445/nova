package com.nova.tools.utils.hutool.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * 测试带毫秒的日期转换
 */
public class IssueI3BS4S {

    @Test
    public void toBeanTest() {
        String jsonStr = "{date: '2021-03-17T06:31:33.99'}";
        final Bean1 bean1 = new Bean1();
        BeanUtil.copyProperties(JSONUtil.parseObj(jsonStr), bean1);
        Assert.equals("2021-03-17T06:31:33.099", bean1.getDate().toString());
    }

    @Data
    public static class Bean1 {
        private LocalDateTime date;
    }
}
