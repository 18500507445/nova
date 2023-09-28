package com.nova.tools.utils.hutool.setting;

import cn.hutool.core.lang.Assert;
import cn.hutool.setting.SettingUtil;
import org.junit.jupiter.api.Test;

public class SettingUtilTest {

    @Test
    public void getTest() {
        String driver = SettingUtil.get("test").get("demo", "driver");
        Assert.equals("com.mysql.jdbc.Driver", driver);
    }

    @Test
    public void getTest2() {
        String driver = SettingUtil.get("example/example").get("demo", "key");
        Assert.equals("value", driver);
    }

    @Test
    public void getFirstFoundTest() {
        //noinspection ConstantConditions
        String driver = SettingUtil.getFirstFound("test2", "test")
                .get("demo", "driver");
        Assert.equals("com.mysql.jdbc.Driver", driver);
    }
}
