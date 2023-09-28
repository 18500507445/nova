package com.nova.tools.utils.hutool.db;

import cn.hutool.core.lang.Assert;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.nova.tools.utils.hutool.db.pojo.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Entity测试
 *
 * @author looly
 */
public class FindBeanTest {

    Db db;

    @Before("")
    public void init() {
        db = Db.use("test");
    }

    @Test
    public void findAllBeanTest() throws SQLException {
        List<User> results = db.findAll(Entity.create("user"), User.class);

        Assert.equals(4, results.size());
        Assert.equals(Integer.valueOf(1), results.get(0).getId());
        Assert.equals("张三", results.get(0).getName());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void findAllListTest() throws SQLException {
        List<List> results = db.findAll(Entity.create("user"), List.class);

        Assert.equals(4, results.size());
        Assert.equals(1, results.get(0).get(0));
        Assert.equals("张三", results.get(0).get(1));
    }

    @Test
    public void findAllArrayTest() throws SQLException {
        List<Object[]> results = db.findAll(Entity.create("user"), Object[].class);

        Assert.equals(4, results.size());
        Assert.equals(1, results.get(0)[0]);
        Assert.equals("张三", results.get(0)[1]);
    }

    @Test
    public void findAllStringTest() throws SQLException {
        List<String> results = db.findAll(Entity.create("user"), String.class);
        Assert.equals(4, results.size());
    }

    @Test
    public void findAllStringArrayTest() throws SQLException {
        List<String[]> results = db.findAll(Entity.create("user"), String[].class);

        Assert.equals(4, results.size());
        Assert.equals("1", results.get(0)[0]);
        Assert.equals("张三", results.get(0)[1]);
    }
}
