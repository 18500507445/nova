package com.nova.search.elasticsearch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author: wzh
 * @description: Es仓库注解类
 * @date: 2024/04/28 15:58
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface EsRepository {

    Class<?> value() default Object.class;

    String name() default "";
}
