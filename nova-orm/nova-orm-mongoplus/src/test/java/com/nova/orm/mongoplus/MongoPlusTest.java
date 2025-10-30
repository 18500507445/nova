package com.nova.orm.mongoplus;

import cn.hutool.json.JSONUtil;
import com.mongoplus.conditions.query.LambdaQueryChainWrapper;
import com.mongoplus.model.PageParam;
import com.nova.common.utils.random.RandomUtils;
import com.nova.orm.mongoplus.repository.User;
import com.nova.orm.mongoplus.repository.UserMapper;
import com.nova.orm.mongoplus.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzh
 * @description: mongoplus-测试类
 * @date: 2025/10/30 16:44
 */
@SpringBootTest
public class MongoPlusTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Test
    public void testInsert() {
        List<User> list = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            User user = new User();
            user.setId(i);
            user.setName(RandomUtils.randomName());
            user.setAge(RandomUtils.randomLong(15, 30));
            user.setAddress(RandomUtils.randomAddress());
            list.add(user);
        }
        Boolean flag = userMapper.saveBatch(list);
        System.out.println("flag = " + flag);
    }


    @Test
    public void testQuery() {
        final PageParam pageParam = new PageParam(1, 10);
        LambdaQueryChainWrapper<User> query = userService.lambdaQuery()
                .in(User::getId, 1, 5);
        List<User> list = userService.pageList(query, pageParam);
        long count = userService.count(query);
        System.err.println("list = " + JSONUtil.toJsonStr(list));
        System.err.println("count = " + count);
    }

}
