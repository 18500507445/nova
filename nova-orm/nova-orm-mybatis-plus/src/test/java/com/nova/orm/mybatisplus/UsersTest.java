package com.nova.orm.mybatisplus;

import cn.hutool.core.collection.ListUtil;
import com.nova.orm.mybatisplus.users.Users;
import com.nova.orm.mybatisplus.users.UsersService;
import com.nova.orm.mybatisplus.utils.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: wzh
 * @description: c
 * @date: 2024/08/27 16:53
 */
@SpringBootTest
public class UsersTest {

    @Resource
    private UsersService usersService;

    @Test
    public void demoA() {
        List<Users> list = new ArrayList<>();
        for (int i = 1; i <= 4000000; i++) {
            Users user = new Users();
            user.setUserName(RandomUtils.randomName());
            user.setPhone(RandomUtils.randomPhone());
            user.setPassword(RandomUtils.randomString(12));
            user.setIdCard(RandomUtils.randomIdCard());
            user.setAddress(RandomUtils.randomAddress());
            user.setSex(RandomUtils.randomInt(1, 3));
            user.setAge(RandomUtils.randomInt(18, 100));
            user.setCreateTime(new Date());
            list.add(user);
        }
        List<List<Users>> split = ListUtil.split(list, 2000);
        for (List<Users> users : split) {
            boolean b = usersService.saveBatch(users);
            System.err.println(b ? "插入成功" : "插入失败");
        }
    }

}
