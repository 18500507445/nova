package com.nova.book.design.structure.adapter.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.nova.book.design.structure.adapter.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author: wzh
 * @description 用户Service实现类
 * @date: 2023/10/25 17:26
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 登录
     * (1)验证数据库
     * (2)添加缓存
     * (3)生成token
     */
    @Override
    public String login(String username, String password) {
        if (ObjectUtil.isAllEmpty(username, password)) {
            return "login success";
        }
        return "login fail";
    }

    /**
     * 注册
     * (1)保存用户数据
     */
    @Override
    public String register(String username, String password) {
        if (ObjectUtil.isAllEmpty(username, password)) {
            return "refresh success";
        }
        return "refresh fail";
    }
}
