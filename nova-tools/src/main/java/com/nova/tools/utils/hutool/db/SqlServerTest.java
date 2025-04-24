package com.nova.tools.utils.hutool.db;

import cn.hutool.core.lang.Console;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.PageResult;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * SQL Server操作单元测试
 *
 * @author:looly
 */
public class SqlServerTest {

    @Test
    public void createTableTest() throws SQLException {
        Db.use("sqlserver").execute("create table T_USER(ID bigint, name varchar(255))");
    }

    @Test
    public void insertTest() throws SQLException {
        for (int id = 100; id < 200; id++) {
            Db.use("sqlserver").insert(Entity.create("T_USER")//
                    .set("ID", id)//
                    .set("name", "测试用户" + id)//
            );
        }
    }

    @Test
    public void pageTest() throws SQLException {
        PageResult<Entity> result = Db.use("sqlserver").page(Entity.create("T_USER"), new Page(2, 10));
        for (Entity entity : result) {
            Console.log(entity.get("ID"));
        }
    }

}
