package com.nova.login.security.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

/**
 * @author: wzh
 * @description: 授权
 * @date: 2024/12/23 15:35
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api")
public class OauthController {

    public String getAccessToken(Principal principal, @RequestParam Map<String, String> params) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return "";
    }
}
