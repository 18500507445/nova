package com.nova.shopping.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @description: 限流
 * @author: wzh
 * @date: 2023/4/14 18:15
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {

    int seconds();

    int maxCount();

    /**
     * 提示消息
     */
    String message() default "超出访问次数，已被限流";
}
