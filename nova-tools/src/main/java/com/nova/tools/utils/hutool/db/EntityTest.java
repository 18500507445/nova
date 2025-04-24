package com.nova.tools.utils.hutool.db;

import cn.hutool.core.lang.Assert;
import cn.hutool.db.Entity;
import com.nova.tools.utils.hutool.db.pojo.User;
import org.junit.jupiter.api.Test;

/**
 * Entity测试
 *
 * @author:looly
 */
public class EntityTest {

    @Test
    public void parseTest() {
        User user = new User();
        user.setId(1);
        user.setName("test");

        Entity entity = Entity.create("testTable").parseBean(user);
        Assert.equals(Integer.valueOf(1), entity.getInt("id"));
        Assert.equals("test", entity.getStr("name"));
    }

    @Test
    public void parseTest2() {
        User user = new User();
        user.setId(1);
        user.setName("test");

        Entity entity = Entity.create().parseBean(user);
        Assert.equals(Integer.valueOf(1), entity.getInt("id"));
        Assert.equals("test", entity.getStr("name"));
        Assert.equals("user", entity.getTableName());
    }

    @Test
    public void parseTest3() {
        User user = new User();
        user.setName("test");

        Entity entity = Entity.create().parseBean(user, false, true);

        Assert.isFalse(entity.containsKey("id"));
        Assert.equals("test", entity.getStr("name"));
        Assert.equals("user", entity.getTableName());
    }

    @Test
    public void entityToBeanIgnoreCaseTest() {
        Entity entity = Entity.create().set("ID", 2).set("NAME", "testName");
        User user = entity.toBeanIgnoreCase(User.class);

        Assert.equals(Integer.valueOf(2), user.getId());
        Assert.equals("testName", user.getName());
    }
}
