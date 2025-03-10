package com.nova.tools.utils.hutool.db;

import cn.hutool.core.lang.Assert;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class UpdateTest {

    Db db;

    @Test
    public void init() {
        db = Db.use("test");
    }

    /**
     * 对更新做单元测试
     *
     * @throws SQLException SQL异常
     */
    @Test
    public void updateTest() throws SQLException {

        // 改
        int update = db.update(Entity.create("user").set("age", 88), Entity.create().set("name", "unitTestUser"));
        Assert.isTrue(update > 0);
        Entity result2 = db.get("user", "name", "unitTestUser");
        Assert.equals(88, result2.getInt("age"));
    }
}
