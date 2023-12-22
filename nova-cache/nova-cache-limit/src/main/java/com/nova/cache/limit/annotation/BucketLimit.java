package com.nova.cache.limit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @description: 令牌桶
 * @author: wzh
 * @date: 2023/4/18 23:56
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface BucketLimit {

    /**
     * 每秒产生的令牌数
     */
    int rate();

    /**
     * 最大容量
     */
    int maxCount();

    /**
     * 请求的令牌数
     */
    int requestNum();

    /**
     * 提示消息
     */
    String message() default "超出访问次数，已被限流";

}
