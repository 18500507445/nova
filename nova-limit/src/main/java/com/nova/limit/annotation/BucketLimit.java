package com.nova.limit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @description: 令牌桶限流
 * @author: wzh
 * @date: 2023/4/18 22:05
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface BucketLimit {

    /**
     * 流速 每秒几个令牌
     */
    int seconds();

    /**
     * 最大容量
     */
    int maxCount();

    /**
     * 提示消息
     */
    String message() default "超出访问次数，已被限流";
}
