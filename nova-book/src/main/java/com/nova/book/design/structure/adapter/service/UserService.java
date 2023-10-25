package com.nova.book.design.structure.adapter.service;

/**
 * @author: wzh
 * @description 用户Service
 * @date: 2023/10/25 17:25
 */
public interface UserService {

    /**
     * 登录
     */
    String login(String username, String password);

    /**
     * 注册
     */
    String register(String username, String password);
}
