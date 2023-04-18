package com.nova.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 单机令牌桶限流注解
 * @author: wzh
 * @date: 2022/10/13 20:26
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtRateLimiter {

    //以每秒为单位固定指定的速率往令牌桶里添加令牌
    double value();

    //毫秒 表示 从桶中拿到令牌等待时间. 如果获取不到令牌 设置服务降级处理(相当于配置在规定时间内如果没有获取到令牌的话，直接走服务降级 )
    long timeOut();

}
