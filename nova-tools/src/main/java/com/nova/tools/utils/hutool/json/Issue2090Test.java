package com.nova.tools.utils.hutool.json;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * https://github.com/dromara/hutool/issues/2090
 */
public class Issue2090Test {

    @Test
    public void parseTest() {
        final TestBean test = new TestBean();
        test.setLocalDate(LocalDate.now());

        final JSONObject json = JSONUtil.parseObj(test);
        final TestBean test1 = json.toBean(TestBean.class);
        Assert.equals(test, test1);
    }

    @Test
    public void parseLocalDateTest() {
        LocalDate localDate = LocalDate.now();
        final JSONObject jsonObject = JSONUtil.parseObj(localDate);
        Assert.notNull(jsonObject.toString());
    }

    @Test
    public void toBeanLocalDateTest() {
        LocalDate d = LocalDate.now();
        final JSONObject obj = JSONUtil.parseObj(d);
        LocalDate d2 = obj.toBean(LocalDate.class);
        Assert.equals(d, d2);
    }

    @Test
    public void toBeanLocalDateTimeTest() {
        LocalDateTime d = LocalDateTime.now();
        final JSONObject obj = JSONUtil.parseObj(d);
        LocalDateTime d2 = obj.toBean(LocalDateTime.class);
        Assert.equals(d, d2);
    }

    @Test
    public void toBeanLocalTimeTest() {
        LocalTime d = LocalTime.now();
        final JSONObject obj = JSONUtil.parseObj(d);
        LocalTime d2 = obj.toBean(LocalTime.class);
        Assert.equals(d, d2);
    }

    @Test
    public void monthTest() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.set("month", Month.JANUARY);
        Assert.equals("{\"month\":1}", jsonObject.toString());
    }

    @Data
    public static class TestBean {
        private LocalDate localDate;
    }
}
