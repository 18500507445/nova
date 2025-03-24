package com.nova.login.security.controller;

import com.nova.common.core.model.result.ResResult;
import com.nova.login.security.core.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description: 用户Controller
 * @date: 2025/03/24 15:29
 */
@Slf4j(topic = "UserController")
@RestController
@RequestMapping("/api/")
public class UserController {

    /**
     * 登录
     * @description: 访问慢开启vpn，静态资源刷不出来
     */
    @RequestMapping(value = "login")
    public ResResult<Void> login() {
        Object o = UserContext.currentUser();
        System.err.println("o = " + o);

        Authentication authentication = UserContext.getAuthentication();
        System.err.println("authentication = " + authentication);

        boolean flag = UserContext.isAuthentication();
        System.err.println("flag = " + flag);

        Object principal = UserContext.getPrincipal();
        System.err.println("principal = " + principal);
        return ResResult.success();
    }
}
