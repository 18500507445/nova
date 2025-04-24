package com.nova.shopping.common.annotation;

import java.lang.annotation.*;

/**
 * @author:wzh
 * @description: 分布式锁注解
 * @date:2023/03/26 20:53
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
