package com.nova.tools.utils.hutool.db;

import cn.hutool.db.Entity;
import cn.hutool.db.Session;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * 事务性数据库操作单元测试
 *
 * @author: looly
 */
public class SessionTest {

    @Test
    public void transTest() {
        Session session = Session.create("test");
        try {
            session.beginTransaction();
            session.update(Entity.create().set("age", 76), Entity.create("user").set("name", "unitTestUser"));
            session.commit();
        } catch (SQLException e) {
            session.quietRollback();
        }
    }

    @Test
    public void txTest() throws SQLException {
        Session.create("test").tx(session -> session.update(Entity.create().set("age", 78), Entity.create("user").set("name", "unitTestUser")));
    }
}
