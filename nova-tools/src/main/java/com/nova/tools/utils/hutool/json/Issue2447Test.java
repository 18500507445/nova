package com.nova.tools.utils.hutool.json;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class Issue2447Test {

    @Test
    public void addInteger() {
        Time time = new Time();
        time.setTime(LocalDateTime.of(1970, 1, 2, 10, 0, 1, 0));
        String timeStr = JSONUtil.toJsonStr(time);
        Assert.equals("{\"time\":93601000}", timeStr);
        Assert.equals(JSONUtil.toBean(timeStr, Time.class).getTime(), time.getTime());
    }

    static class Time {
        private LocalDateTime time;

        public LocalDateTime getTime() {
            return time;
        }

        public void setTime(LocalDateTime time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return time.toString();
        }
    }

}
