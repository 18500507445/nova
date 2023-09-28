package com.nova.tools.utils.hutool.db.dialect;

import cn.hutool.core.lang.Assert;
import cn.hutool.db.dialect.DriverUtil;
import org.junit.jupiter.api.Test;

public class DriverUtilTest {

    @Test
    public void identifyDriverTest() {
        String url = "jdbc:h2:file:./db/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL";
        // driver 返回 mysql 的 driver
        String driver = DriverUtil.identifyDriver(url);
        Assert.equals("org.h2.Driver", driver);
    }
}
