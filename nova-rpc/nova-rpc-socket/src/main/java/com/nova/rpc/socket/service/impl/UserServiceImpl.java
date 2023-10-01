package com.nova.rpc.socket.service.impl;


import com.nova.rpc.socket.entity.UserBO;
import com.nova.rpc.socket.service.UserService;

import java.util.Random;
import java.util.UUID;

/**
 * @description: user实现类
 * @author: wzh
 * @date: 2023/1/19 19:48
 */
public class UserServiceImpl implements UserService {

    @Override
    public UserBO getUserByUserId(Integer id) {
        System.err.println("客户端查询了" + id + "的用户");
        // 模拟从数据库中取用户的行为
        Random random = new Random();
        return UserBO.builder().userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean()).build();
    }

    @Override
    public Integer insertUserId(UserBO user) {
        System.err.println("插入数据成功：" + user);
        return 1;
    }

    @Override
    public String hello() {
        return "Hello World!";
    }
}
