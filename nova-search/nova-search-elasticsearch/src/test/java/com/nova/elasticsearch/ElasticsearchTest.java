package com.nova.elasticsearch;

import cn.hutool.json.JSONUtil;
import com.nova.elasticsearch.entity.User;
import com.nova.elasticsearch.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description 测试类
 * @date: 2023/07/13 23:00
 */
@SpringBootTest
public class ElasticsearchTest {

    @Resource
    private UserService userService;

    @Test
    public void insertTest() {
        User user = new User();
        user.setId("1");
        user.setUsername("张三");
        user.setPassword("zhangsan");
        userService.save(user);
    }

    @Test
    public void getAllTest() {
        Iterable<User> iterable = userService.getAll();
        for (User user : iterable) {
            System.err.println("jsonUser = " + JSONUtil.toJsonStr(user));
        }
    }

    @Test
    public void deleteTest() {
        User user = new User();
        user.setId("1");
        userService.delete(user);
    }


}
