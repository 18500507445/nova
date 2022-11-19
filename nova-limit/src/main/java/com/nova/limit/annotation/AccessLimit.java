package com.nova.limit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/11/19 16:29
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {

    int seconds();

    int maxCount();

    boolean needLogin()default false;
}
