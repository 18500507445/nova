package com.nova.login.security.core;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户上下文
 * <p>
 *
 * @serial 2.0.0
 * @Author 程序员Mars
 */
@Component
public class UserContext {

    /**
     * 获取当前登录用户的信息，如果未登录返回null
     *
     * @return Object 当前登录用户
     */
    public static Object currentUser() {
        return null != getAuthentication() ? getAuthentication().getPrincipal() : null;
    }

    /**
     * 获取当前登录用户的信息
     *
     * @return Authentication 权鉴对象
     */
    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) ? authentication : null;
    }

    /**
     * 验证当前用户是否登录
     *
     * @return boolean 是否登录
     */
    public static boolean isAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !(auth instanceof AnonymousAuthenticationToken);
    }

    /**
     * SysUser 当前用户
     */
    public static Object getPrincipal() {
        try {
            return Objects.requireNonNull(getAuthentication()).getPrincipal();
        } catch (NullPointerException e) {
            return new Object();
        }
    }
}
