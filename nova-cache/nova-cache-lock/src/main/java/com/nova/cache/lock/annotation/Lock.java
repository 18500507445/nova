package com.nova.cache.lock.annotation;

import java.lang.annotation.*;

/**
 * 分布式锁注解
 *
 * @author: wzh
 * @date: 2022/12/26 20:53
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Lock {

    /**
     * 分布式锁名称
     *
     * @return String
     */
    String value() default "lock-redisson";

    /**
     * 锁超时时间,默认十秒
     *
     * @return int
     */
    int expireSeconds() default 10;
}
