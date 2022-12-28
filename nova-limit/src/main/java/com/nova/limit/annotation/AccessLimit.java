package com.nova.limit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @description: 限流注解
 * @author: wangzehui
 * @date: 2022/11/19 16:29
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {

    int seconds();

    int maxCount();

    /**
     * 是否需要登录
     *
     * @return
     */
    boolean needLogin() default false;

    /**
     * 提示消息
     */
    String message() default "超出访问次数，已被限流";

}
