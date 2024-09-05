package com.nova.login.security.controller;

import com.nova.common.core.model.result.ResResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wzh
 * @description: 用户Controller
 * @date: 2024/09/04 18:51
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    @RequestMapping("/token")
    public ResResult<Authentication> token() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResResult.success(authentication);
    }
}
