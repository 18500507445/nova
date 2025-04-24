package com.nova.book.design.structure.adapter.controller;

import com.nova.book.design.structure.adapter.LoginAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description: 用户Controller
 * @date: 2023/10/25 17:29
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class UserController {

    private final LoginAdapter loginAdapter;

    /**
     * 账号密码登录
     */
    @PostMapping("/login")
    public String login(String account, String password) {
        return loginAdapter.login(account, password);
    }

    @PostMapping("/register")
    public String register(String account, String password) {
        return loginAdapter.register(account, password);
    }

    /**
     * 适配后的三方登录
     */
    @PostMapping("/wechatLogin")
    public String wechatLogin(String token) {
        return loginAdapter.wechatLogin(token);
    }

}
